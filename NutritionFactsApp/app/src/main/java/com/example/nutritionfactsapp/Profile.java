package com.example.nutritionfactsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {

    TextView name, email;
    Button logout;

    String userID;

    FirebaseAuth mAuth;
    FirebaseFirestore cloudDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.textProfileName);
        email = findViewById(R.id.textProfileEmail);

        mAuth = FirebaseAuth.getInstance();
        cloudDB = FirebaseFirestore.getInstance();

        logout = findViewById(R.id.buttonLogout);

        // User is allow to log out of the account
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Profile.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, Login.class));
            }
        });


        // Getting user data from the fire store to display in profile
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = cloudDB.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("Full_Name"));
                email.setText(value.getString("Email"));

                System.out.println(name);
            }
        });
    }
}