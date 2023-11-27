package com.example.annex8;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout LinearAnimator;

    Boolean reverse;
    ObjectAnimator anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearAnimator = findViewById(R.id.LinearAnimator);

        reverse = false;


        LinearAnimator.setOnClickListener(source ->{

            if(!reverse) {
                anim.start();
                reverse = reverse;
            }else{
                anim.reverse();
            }
        });

        anim = ObjectAnimator.ofFloat(LinearAnimator, View.TRANSLATION_Y, 0);
    }
}