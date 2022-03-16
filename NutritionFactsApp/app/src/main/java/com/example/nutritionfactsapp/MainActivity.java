package com.example.nutritionfactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

// This activity displays the main list when the app is loaded.
public class MainActivity extends AppCompatActivity {

    // Variable declaration
    RecyclerView recyclerView;

    private ArrayList<String> titles;
    private ArrayList<String> grades;
    private ArrayList<String> images;
    private ArrayList<String> ingredients;
    private ArrayList<String> nova_groups;
    private ArrayList<String> barcodes;
    private ArrayList<String> nutrients;

    DatabaseHelper myDB; // Database
    ItemAdapter adapter; // item adapter to display item
    FloatingActionButton openList, openSearch, openProfile; // buttons for directing to other screens

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titles = new ArrayList<>(); // Initialise array list
        grades = new ArrayList<>();
        images = new ArrayList<>();
        ingredients = new ArrayList<>();
        nova_groups = new ArrayList<>();
        barcodes = new ArrayList<>();
        nutrients = new ArrayList<>();


        recyclerView = findViewById(R.id.recycleView);
        adapter = new ItemAdapter(this, titles, grades, ingredients, nova_groups, barcodes, nutrients, images);
        myDB = new DatabaseHelper(this);


        // Hardcoded values for testing before the API was implemented
//        myDB.addItem("Cornflakes", "B", "2","1234567","corn", "protein 4g"); // Adding data to the database
//        myDB.addItem("Jam", "C", "3","12332267","jam, others", "protein 4g , carb 3g");
//        myDB.addItem("Marmite", "A", "2","856342","mite, water", "protein 7g");
//
//        myDB.addItem("Pringles", "E", "1","321654","potato", "protein 3g");
//        myDB.addItem("Milo", "B", "4","001254","choco,milk", "protein 8g");
//        myDB.addItem("Coca cola", "D", "3","099054","water", "protein 1.5g");


        getDisplayItems(); // from the local database retrieve
       // getDisplayItemCloud(); // from the cloud database retrieve

        // Hardcoded images for testing before the API was implemented
//        images.add(R.drawable.cornflakes);
//        images.add(R.drawable.jam);
//        images.add(R.drawable.marmite);
//        images.add(R.drawable.milo);
//        images.add(R.drawable.pringles);
//        images.add(R.drawable.coca_cola);


        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // opening the custom list
        openList = findViewById(R.id.list_open);
        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomList.class);
                startActivity(intent);
            }
        });

        // opening the search screen
        openSearch = findViewById(R.id.search_button);
        openSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this, SearchDisplay.class);
                startActivity(searchIntent);
            }
        });

        //opening the profile screen
        openProfile = findViewById(R.id.profile_button);
        openProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

    }

    // read all the data from the local DB
    void getDisplayItems() {
        Cursor cursor = myDB.readAllProductData();
        if (cursor.getCount() == 0) {   // checking for availability
            Toast.makeText(this, "No items.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                titles.add(cursor.getString(1));
                grades.add(cursor.getString(2));
                ingredients.add(cursor.getString(4));
                nova_groups.add(cursor.getString(3));
                barcodes.add(cursor.getString(0));
                nutrients.add(cursor.getString(5));
                images.add(cursor.getString(6));
            }

        }
    }

    // read data from the cloud DB
    void getDisplayItemCloud() {
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid(); // getting the registered users id to store in the database

        //cloudDB.collection("Products").get()
        cloudDB.collection("UserData").document(userId).collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            titles.add(snapshot.getString("title"));
                            grades.add(snapshot.getString("grade"));
                            ingredients.add(snapshot.getString("ingredients"));
                            nova_groups.add(snapshot.getString("nova_group"));
                            barcodes.add(snapshot.getString("barcode"));
                            nutrients.add(snapshot.getString("nutrient"));
                            images.add(snapshot.getString("imageURl"));

                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Successfully retrieve from cloud!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}