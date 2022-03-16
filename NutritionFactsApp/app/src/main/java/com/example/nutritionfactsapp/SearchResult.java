package com.example.nutritionfactsapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

// This activity is to save the data from the search product to our main list.
public class SearchResult extends AppCompatActivity {

    //Variable declaration
    ImageView mainImageView;
    TextView titleR, gradeR, ingredientR, nova_groupR, barcodeR, nutrientR;
    Button save;
    Button cancel;

    String Title, Grade, Ingredient, Nova_group, Barcode, Nutrient, ImageURL;

    DatabaseHelper myDB;
    String userId;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        myDB = new DatabaseHelper(this);


        mainImageView = findViewById(R.id.image_result);
        titleR = findViewById(R.id.title1);
        gradeR = findViewById(R.id.grade1);
        ingredientR = findViewById(R.id.ingredient1);
        nova_groupR = findViewById(R.id.nova_group1);
        barcodeR = findViewById(R.id.barcode1);
        nutrientR =findViewById(R.id.nutrient1);
        save = findViewById(R.id.save_button);
        cancel = findViewById(R.id.cancel_button);

        mAuth = FirebaseAuth.getInstance();

        getData();
        setData();
        Intent intent= new Intent(SearchResult.this, MainActivity.class);

        // Saving to the database
        save.setOnClickListener(new View.OnClickListener() { // user can save to the main list
            @Override
            public void onClick(View v) {
                myDB.addItem(Title, Grade,Nova_group,Barcode,Ingredient, Nutrient,ImageURL); // storing to the SQLite database
                fireStoreAdd(); // storing to the fire store database
                Toast.makeText(SearchResult.this,"Successfully Added", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // Cancel returns to home
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);

            }
        });

    }

    // get the data from the API
    private void getData(){
            Title = getIntent().getStringExtra("titles");
            Grade = getIntent().getStringExtra("grades");
            Ingredient = getIntent().getStringExtra("ingredients");
            Nova_group = getIntent().getStringExtra("nova_groups");
            Barcode = getIntent().getStringExtra("barcodes");
            Nutrient = getIntent().getStringExtra("nutrients");
            ImageURL = getIntent().getStringExtra("images");

            System.out.println(Barcode);


    }

    // set to the screen
    private void setData(){
        titleR.setText(Title);
        gradeR.setText(Grade);
        ingredientR.setText(Ingredient);
        nova_groupR.setText(Nova_group);
        barcodeR.setText(Barcode);
        nutrientR.setText(Nutrient);

        Picasso.get().load(ImageURL).fit().centerInside().into(mainImageView);



    }

    // Saving product detail from the API to cloud database fire store
    private void fireStoreAdd(){
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();

        HashMap<String, Object> product = new HashMap<>();
        product.put("title", Title);
        product.put("grade", Grade);
        product.put("ingredients", Ingredient);
        product.put("nova_group", Nova_group);
        product.put("barcode", Barcode);
        product.put("nutrient", Nutrient);
        product.put("imageURl", ImageURL);

        userId = mAuth.getCurrentUser().getUid(); // getting the registered users id to store in the database


        //cloudDB.collection("Products").document(Barcode).set(product)
        cloudDB.collection("UserData").document(userId).collection("Products").document(Barcode).set(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SearchResult.this,"Saved to Cloud !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(SearchResult.this, "Failed to save to cloud !!",Toast.LENGTH_SHORT).show();
            }
        });

    }


}