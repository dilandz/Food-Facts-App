package com.example.nutritionfactsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddList extends AppCompatActivity {

    // Variable declaration
    EditText list_name, description;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        list_name = findViewById(R.id.list_name);// Getting the ID values
        description = findViewById(R.id.list_Desc);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() { //Adds the details to the DB
            @Override
            public void onClick(View view) {
                DatabaseHelper myDB = new DatabaseHelper(AddList.this); //object
                myDB.addList(list_name.getText().toString().trim(),description.getText().toString().trim());
                Intent intent = new Intent(AddList.this,CustomList.class);
                startActivity(intent);

            }
        });
    }
}