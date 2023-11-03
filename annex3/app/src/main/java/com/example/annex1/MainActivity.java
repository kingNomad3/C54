package com.example.annex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button ajouter;
    Button afficher;
    Button quitter;
    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ec = new Ecouteur();

        ajouter = findViewById(R.id.ajouter);
        afficher =findViewById(R.id.afficher);
        quitter = findViewById(R.id.quitter);

        ajouter.setOnClickListener(ec);
        afficher.setOnClickListener(ec);
        quitter.setOnClickListener(ec);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //recuper la liste dans le fichier serialisation s'il existe
        try {
            ArrayList<String> liste =  SingletonMemo.getInstance(getApplicationContext()).deSeriazableListe();
            SingletonMemo.getInstance(this).setListe(liste);

        }catch (Exception e){ // pas de ficher
            e.printStackTrace();
        }
    }

    private class Ecouteur implements AdapterView.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v == ajouter){
                Intent i = new Intent(MainActivity.this, activity_ajouter.class);
                startActivity(i);
            }else if (v == afficher){
                Intent i = new Intent(MainActivity.this,AfficherActivity.class);
                startActivity(i);
            }else if(v == quitter){
                finish();
            }
        }
    }
}