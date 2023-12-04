package com.example.exam2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    ObjectAnimator anim;
    AnimatorSet scaleUpSet = new AnimatorSet();
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.imageView);



        Vector<String> listCoordonnes = new Vector<>();
        try {
            listCoordonnes = recupererCoordonnes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(listCoordonnes);

        Path p = new Path();


        for (int i =0; i<7;i++){
            p.lineTo(500,1000);
            p.lineTo(350,800);
            p.lineTo(250,600);
            p.lineTo(375,350);
            p.lineTo(500,420);
            p.lineTo(580,350);
            p.lineTo(760,400);
            p.lineTo(735,600);
            p.lineTo(610,800);
            p.lineTo(500,1000);


        }
//        float x = 0;
//        float y = 0;

//        for( int i =0; i <= listCoordonnes.size(); i++){
//           if (listCoordonnes.indexOf(i) % 2 == 0 ){
//               //Mon delimiteur ne semble pas fonctionner
//               //j'aurai utiliser un modulo pour separer mes x et y par la suite
//               //et donner les coordoner au moveTo/Line to
////                x= Float.parseFloat(listCoordonnes.get(i));
//
//           }else {
////              y = Float.parseFloat(listCoordonnes.get(i));
//           }
//
//           p.moveTo(x,y);
//           p.lineTo(x,y);
//
//        }


        anim = ObjectAnimator.ofFloat(image, View.X,View.Y,p);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(image, "scaleX", 1f, 25f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(image, "scaleY", 1f, 25f);


        scaleUpSet.playTogether(scaleUpX, scaleUpY);
        scaleUpSet.setDuration(1000);

        AnimatorSet sequenceSet = new AnimatorSet();
        sequenceSet.playSequentially( anim, scaleUpSet);

        sequenceSet.start();


    }



    public Vector<String> recupererCoordonnes(){
        Scanner sc = null;
        Vector<String> temp = new Vector<>();

        try {
            InputStream fis =getResources().openRawResource(R.raw.coordonnees);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();

            while (line!= null){
                sc = new Scanner(line);
                sc.useDelimiter("#");

                while (sc.hasNext()){
                    sc.next();

                }
                line = br.readLine();
                temp.add(line);

            }
            fermerFlux(br);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return temp;
    }


    public void fermerFlux (BufferedReader br){
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
