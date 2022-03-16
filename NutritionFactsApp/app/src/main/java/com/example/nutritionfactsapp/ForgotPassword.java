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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPassword extends AppCompatActivity {

    EditText email_reset;
    Button reset;

    String email;

    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_reset = findViewById(R.id.ForgotEmailAddress);
        reset = findViewById(R.id.ResetButton);

        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
                startActivity(new Intent(ForgotPassword.this,Login.class));
                finish();
            }
        });


    }

    private void resetPassword(){
        email = email_reset.getText().toString().trim();

        if (email.isEmpty()){
            email_reset.setError("Email is required!");
            email_reset.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_reset.setError("Please provide a valid email!");
            email_reset.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                if ( task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email for the reset link!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "Try again! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}