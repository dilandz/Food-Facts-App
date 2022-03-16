package com.example.nutritionfactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.HashMap;

public class Sign_up extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText name, sign_email, sign_password;
    TextView loginPage;
    Button create;
    ProgressBar progressBar;

    String Name, Email, Password, UserID;
    FirebaseAuth mAuth;
    FirebaseFirestore cloudDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.PersonName);
        sign_email = findViewById(R.id.SignUPEmailAddress);
        sign_password = findViewById(R.id.SignUpPassword);

        create = findViewById(R.id.SignUpButton2);
        loginPage = findViewById(R.id.textLogin);
        progressBar = findViewById(R.id.progressBar3);

        mAuth = FirebaseAuth.getInstance();
        cloudDB = FirebaseFirestore.getInstance();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_up.this,Login.class));
            }
        });

    }

    private void registerUser(){
        Name = name.getText().toString().trim();
        Email = sign_email.getText().toString().trim();
        Password = sign_password.getText().toString().trim();

        if(Name.isEmpty()){
            name.setError("Full name is required!");
            name.requestFocus();
            return;
        }

        if (Email.isEmpty()){
            sign_email.setError("Email is required!");
            sign_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            sign_email.setError("Please provide a valid email!");
            sign_email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            sign_password.setError("Password is required!");
            sign_password.requestFocus();
            return;
        }

        if(Password.length() < 6){
            sign_password.setError("Min password length is 6 characters");
            sign_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email,Password) // registering the user with fire store and storing to the database
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull  Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(Sign_up.this, "User Created", Toast.LENGTH_SHORT).show();

                        //Storing to fire store
                        UserID = mAuth.getCurrentUser().getUid(); // getting the registered users id to store in the database
                        DocumentReference documentReference = cloudDB.collection("Users").document(UserID);
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("Full_Name", Name);
                        user.put("Email", Email);
                        user.put("Password", Password);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"onSuccess : user profile is created for " +UserID );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onFailure : " + e.toString());
                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }else{
                        Toast.makeText(Sign_up.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });

    }

}