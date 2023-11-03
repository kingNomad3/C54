package com.example.annex1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kotlinx.coroutines.selects.WhileSelectKt;

public class AfficherActivity extends AppCompatActivity {


    ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher);

        liste = findViewById(R.id.liste);
        ArrayAdapter adapteur = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SingletonMemo.getInstance(getApplicationContext()).getListe());
        liste.setAdapter(adapteur);
    }

    @Override

    protected void onStop(){
        super.onStop();

        try {
            SingletonMemo.getInstance(getApplicationContext()).seriazableListe();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}