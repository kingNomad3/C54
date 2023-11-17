package com.example.appchronometre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {
    Chronometer chrono;
    Button boutonStart, boutonStop, pauseButton;

    boolean pause;
    long tempsEcouler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chrono = findViewById(R.id.chrono);

        boutonStart = findViewById(R.id.boutonStart);
        boutonStop = findViewById(R.id.boutonStop);
        pauseButton = findViewById(R.id.pause);




        boutonStart.setOnClickListener( source -> {
            chrono.setBase(SystemClock.elapsedRealtime()); // permet de au chrono de commencer lorsqu'on click sinon va commecner lorsque l'activiter commence
            chrono.start();
        });

        boutonStop.setOnClickListener( source -> {
            chrono.stop();

        });

        pauseButton.setOnClickListener(source ->{
            pause  =!pause; // inverse
            if (pause){
                tempsEcouler = SystemClock.elapsedRealtime() - chrono.getBase();
                chrono.stop();
                pauseButton.setText("redemarer");
            }else {
                chrono.setBase(SystemClock.elapsedRealtime() - tempsEcouler);
                chrono.start();
                pauseButton.setText("pause");
            }



        });



    }
}