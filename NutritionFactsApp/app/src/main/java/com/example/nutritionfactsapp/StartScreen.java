package com.example.nutritionfactsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreen<topAnim> extends AppCompatActivity {

    Handler handler;

    ImageView logo;
    Animation topAnim;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(StartScreen.this,Login.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(StartScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000); //start the next activity after this delay time


        mAuth = FirebaseAuth.getInstance();

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);

        logo = findViewById(R.id.imageViewlogo);
        logo.setAnimation(topAnim);
    }





}