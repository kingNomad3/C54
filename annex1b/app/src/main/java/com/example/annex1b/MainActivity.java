package com.example.annex1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        TextView qa, qb,qc,qd;
        Button buttonA;
        Ecouteur ec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qa = findViewById(R.id.qa);
        qb = findViewById(R.id.qb);
        qc = findViewById(R.id.qc);
        qd = findViewById(R.id.qd);

        buttonA =findViewById(R.id.buttonA);


        ec = new Ecouteur();

        buttonA.setOnClickListener(ec);


    }

    private class Ecouteur implements AdapterView.OnClickListener{

        @Override
        public void onClick(View v) {

            if (v == buttonA){
            }

        }


    }

    public int questionA(){
        String fileName = "files.txt";
        int nbligne =0;


        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while (line != null){

                line = br.readLine();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return nbligne;


    }





}