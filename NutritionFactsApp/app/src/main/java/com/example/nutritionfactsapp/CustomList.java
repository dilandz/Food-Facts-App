package com.example.nutritionfactsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// This takes all the data from the database and display all the custom list made.
public class CustomList extends AppCompatActivity {

    //Variable Declaration
    RecyclerView recyclerView2;
    FloatingActionButton add_list;

    DatabaseHelper myDB;
    ArrayList<String> name,description;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        recyclerView2 = findViewById(R.id.recycleView2);

        add_list = findViewById(R.id.add_list);
        add_list.setOnClickListener(new View.OnClickListener() { //Directing to the add form screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomList.this,AddList.class);
                startActivity(intent);
            }
        });

        myDB = new DatabaseHelper(CustomList.this);
        name = new ArrayList<>();
        description = new ArrayList<>();

        displayList();
        listAdapter = new ListAdapter(CustomList.this, name, description);
        recyclerView2.setAdapter(listAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

    }


    // Getting the details from the DB about all the lists
    void displayList(){
        Cursor cursor2 = myDB.readAllListData();
        if(cursor2.getCount() == 0){   // checking for availability
            Toast.makeText(this,"No items.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor2.moveToNext()){
                name.add(cursor2.getString(1));
                description.add(cursor2.getString(2));

            }

        }
    }
}