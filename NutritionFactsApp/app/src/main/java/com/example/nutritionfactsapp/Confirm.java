package com.example.nutritionfactsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// This class gets the data from the api and provide to the display with a confirmation dialog
public class Confirm extends AppCompatActivity {

    //Variable Declaration
    OpenFoodFactAPI api;
    String id;
    String name, grade, ingredient, nova_group, barcode, nutrient, imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);


        id =getIntent().getStringExtra("id"); //getting the barcode from other class

        api = new OpenFoodFactAPI(Confirm.this, id);
        api.jsonParse(); // take data from the url


        // Dialog to view the details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information is Loaded ");
        builder.setMessage("Details about the product is being loaded from the open food fact database.");
        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = api.getName(); //Getting the details from the API
                grade = api.getGrade();
                ingredient = api.getIngredient();
                barcode =api.getBarcode();
                nova_group= api.getNova_group();
                nutrient = api.getNutrient();
                imageURL = api.getImageURL();

                Intent intent =new Intent(Confirm.this,SearchResult.class);

                intent.putExtra("titles",name);
                intent.putExtra("grades",grade);
                intent.putExtra("ingredients",ingredient);
                intent.putExtra("barcodes",barcode);
                intent.putExtra("nova_groups",nova_group);
                intent.putExtra("nutrients",nutrient);
                intent.putExtra("images",imageURL);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();




    }
}