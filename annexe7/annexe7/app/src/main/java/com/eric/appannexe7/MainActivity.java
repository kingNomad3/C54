package com.eric.appannexe7;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button acheter,appeler,ouEst,photo,message;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        acheter = findViewById(R.id.boutonLivre);
        appeler = findViewById(R.id.boutonAppel);
        ouEst = findViewById(R.id.boutonVille);
        photo = findViewById(R.id.boutonPhoto);
        message = findViewById(R.id.boutonMessage);
        image = findViewById(R.id.imageView);


        acheter.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse( "https://www.leslibraires.ca/"));
            startActivities(i);

        });
        appeler.setOnClickListener(source -> {

        });
        ouEst.setOnClickListener(source -> {

        });
        photo.setOnClickListener(source -> {

        });
        message.setOnClickListener(source -> {

        });








    }
}