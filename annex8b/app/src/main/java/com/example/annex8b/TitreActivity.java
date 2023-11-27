package com.example.annex8b;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class TitreActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ObjectAnimator animY;
    ObjectAnimator animX;
    ObjectAnimator animA, animS;
    AnimatorSet aniSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titre);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        aniSet = new AnimatorSet();

        animY = ObjectAnimator.ofFloat(textView,View.SCALE_Y,1);
        animX = ObjectAnimator.ofFloat(textView,View.SCALE_X,1);
        animA = ObjectAnimator.ofFloat(textView,View.ALPHA,1);


        aniSet.setDuration(5000);
        aniSet.setInterpolator(new BounceInterpolator());
        aniSet.playTogether(animX,animY,animA);

        button.setOnClickListener(source ->{
            aniSet.start();
        });


    }
}