package com.example.nutritionfactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Viewing the individual items with its data.
public class DisplayingItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Variable declaration
    ImageView mainImageView;
    TextView title, grade, ingredient, nova_group, barcode, nutrient;

    String Title, Grade, Ingredient, Nova_group, Barcode, Nutrient, Image;
    FloatingActionButton delete, add;

    DatabaseHelper myDB;
    ArrayList<String> name,description;

    String List_id, selectedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_item);

        mainImageView = findViewById(R.id.image); // getting id value
        title = findViewById(R.id.title);
        grade = findViewById(R.id.grade);
        ingredient = findViewById(R.id.ingredient);
        nova_group = findViewById(R.id.nova_group);
        barcode = findViewById(R.id.barcode);
        nutrient = findViewById(R.id.nutrient);

        myDB = new DatabaseHelper(this);
        name = new ArrayList<>();
        description = new ArrayList<>();

        getData(); // get data from db
        setData(); // set data to the screen
        displayList();

        delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() { // for deleting an item
            @Override
            public void onClick(View v) {
                deleteProductList();
            }
        });

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, name);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.add_products_list(List_id,Barcode);
                Toast.makeText(DisplayingItem.this, "Product is added to the " + selectedList + " list", Toast.LENGTH_SHORT).show();
            }
        });





    }

    // Getting data for each item
    private void getData() {
        if (getIntent().hasExtra("images") && getIntent().hasExtra("titles") && getIntent().hasExtra("grades")) {
            Title = getIntent().getStringExtra("titles");
            Grade = getIntent().getStringExtra("grades");
            Ingredient = getIntent().getStringExtra("ingredients");
            Nova_group = getIntent().getStringExtra("nova_groups");
            Barcode = getIntent().getStringExtra("barcodes");
            Nutrient = getIntent().getStringExtra("nutrients");

            Image = getIntent().getStringExtra("images");

        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show(); // if no data in the db
        }
    }

    // set to each field
    private void setData() {
        title.setText(Title);
        grade.setText(Grade);
        ingredient.setText(Ingredient);
        nova_group.setText(Nova_group);
        barcode.setText(Barcode);
        nutrient.setText(Nutrient);

        Picasso.get().load(Image).fit().centerInside().into(mainImageView);// using picasso to load the image from the URL

    }


    // Delete product
    void deleteProductList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + Title + " ? ");
        builder.setMessage("Are you sure you want to delete " + Title + " product ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(DisplayingItem.this);
                myDB.deleteProduct(Title); // Delete the product from the local database
                fireStoreDelete(); // Delete the product from the Cloud database
                Intent intent = new Intent(DisplayingItem.this, MainActivity.class);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }


    // Delete method for deleting product from the fire store database
    private void fireStoreDelete() {
        FirebaseFirestore cloudDB = FirebaseFirestore.getInstance();

        cloudDB.collection("Products").document(Barcode).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DisplayingItem.this, "Product is removed from cloud too !!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DisplayingItem.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // getting the lists to display in the spinner
    void displayList(){
        Cursor cursor2 = myDB.readAllListData();

            while (cursor2.moveToNext()){
                name.add(cursor2.getString(1));
                description.add(cursor2.getString(2));

            }


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedList = parent.getItemAtPosition(position).toString();

        Cursor cursor3 = myDB.getListIDFromListName(selectedList);
        cursor3.moveToNext();
        List_id = cursor3.getString(0);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + selectedList, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}