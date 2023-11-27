package com.example.annex8b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button gDAnimation, titreAnimation,splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gDAnimation = findViewById(R.id.GDAnimation);
        titreAnimation = findViewById(R.id.titreAnimation);
        splash = findViewById(R.id.splash);


        gDAnimation.setOnClickListener(source ->{
            Intent i = new Intent(MainActivity.this,GaucheDroiteActivity.class);
            startActivity(i);
        });

        titreAnimation.setOnClickListener(source ->{
            Intent i = new Intent(MainActivity.this,TitreActivity.class);
            startActivity(i);
        });

        splash.setOnClickListener(source ->{
            Intent i = new Intent(MainActivity.this,SplashActivity.class);
            startActivity(i);
        });
    }
}