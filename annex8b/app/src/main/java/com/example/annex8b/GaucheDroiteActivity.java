package com.example.annex8b;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;



public class GaucheDroiteActivity extends AppCompatActivity {

    Button animationButon;
    View object;
    ObjectAnimator anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauche_droite);

        animationButon = findViewById(R.id.AnimationButton);
        object = findViewById(R.id.object);


        Path p = new Path();
        float y = 500;
        p.moveTo(-600,y);
        p.lineTo(-410,y);
        for (int i =0; i<7;i++){
            p.lineTo(370,y);
            p.lineTo(440,y);
        }

        p.lineTo(1100,y);

        anim = ObjectAnimator.ofFloat(object, View.X,View.Y,p);
        anim.setDuration(1000); // la duree de l'animation
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);


        animationButon.setOnClickListener(source -> {
            anim.start();
        });

    }


}