package com.example.nutritionfactsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DisplayCustomListItem extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper myDB;

    private ArrayList<String> titles;
    private ArrayList<String> grades;
    private ArrayList<String> images;
    private ArrayList<String> ingredients;
    private ArrayList<String> nova_groups;
    private ArrayList<String> barcodes;
    private ArrayList<String> nutrients;

    ItemAdapter adapter; // item adapter to display item

    String list_name,list_id,product_barcode;
    FloatingActionButton deleteList;
    TextView displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_custom_list_item);

        recyclerView = findViewById(R.id.recycleView3);

        titles = new ArrayList<>(); // Initialise array list
        grades = new ArrayList<>();
        images = new ArrayList<>();
        ingredients = new ArrayList<>();
        nova_groups = new ArrayList<>();
        barcodes = new ArrayList<>();
        nutrients = new ArrayList<>();

        displayName = findViewById(R.id.textList);


        adapter = new ItemAdapter(this, titles, grades, ingredients, nova_groups, barcodes, nutrients, images);
        myDB = new DatabaseHelper(this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDisplayItems();

        deleteList = findViewById(R.id.deleteList_button);
        deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomList();
            }
        });

    }


    // read all the data from the local DB
    void getDisplayItems() {

        list_name = getIntent().getStringExtra("listName"); // getting the selected list name
        displayName.setText(list_name);
        Cursor cursor3 = myDB.getListIDFromListName(list_name); // getting the id for the particular list
        cursor3.moveToNext();
        list_id = cursor3.getString(0);

        Cursor cursor1 = myDB.getBarcodeFromListID(list_id); // getting the barcodes in the specific list
        while(cursor1.moveToNext()) { // reading all the barcodes
            product_barcode = cursor1.getString(0);



        Cursor cursor = myDB.getProductFromBarcode(product_barcode); // getting product details relating to specific barcode
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
    }

    // Delete list
    void deleteCustomList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + list_name + " ? ");
        builder.setMessage("Are you sure you want to delete " + list_name + " list ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(DisplayCustomListItem.this);
                myDB.deleteList(list_id); // Delete the product from the local database
                myDB.deleteProductToList(list_id);
                Intent intent = new Intent(DisplayCustomListItem.this, CustomList.class);
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
}