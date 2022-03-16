package com.example.nutritionfactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class Login extends AppCompatActivity {

    EditText email_login, password_login;
    TextView forgot_password;
    Button login, sign_up;

    String email, password;

    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.LoginEmailAddress);
        password_login = findViewById(R.id.LoginPassword);
        forgot_password = findViewById(R.id.textForgot);

        login = findViewById(R.id.LoginButton);
        sign_up = findViewById(R.id.SignUpButton);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        // Take the user to sign page
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Sign_up.class);
                startActivity(intent);
            }
        });

        // User is verified and logged in
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });

        // Takes user to reset the password
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });



    }

    private void userLogin(){
        email=  email_login.getText().toString().trim();
        password = password_login.getText().toString().trim();

        if (email.isEmpty()){
            email_login.setError("Email is required!");
            email_login.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_login.setError("Please provide a valid email!");
            email_login.requestFocus();
            return;
        }

        if(password.isEmpty()){
            password_login.setError("Password is required!");
            password_login.requestFocus();
            return;
        }

        if(password.length() < 6){
            password_login.setError("Min password length is 6 characters");
            password_login.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(Login.this,MainActivity.class));
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Failed to login! check your credentials", Toast.LENGTH_SHORT).show();
                    System.out.println(" failed login");

                }
            }
        });

    }



}