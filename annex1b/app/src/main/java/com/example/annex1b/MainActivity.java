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
import java.util.Scanner;

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
                questionA();
                questionB();
//                questionC();
//                questionD();
            }

        }


    }

    public long questionA(){
        String fileName = "file.txt";
        long nbligne =0;

        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null){
                nbligne++;
                line = br.readLine();
            }
            qa.setText(String.valueOf(nbligne));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbligne;
    }

    public long questionB(){
        String fileName = "file.txt";
        long caractere =0;
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();

            while (line != null){
                caractere+= br.readLine().length();
                line = br.readLine();
            }
            qb.setText(String.valueOf(caractere));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caractere;
    }


    public void questionC(){

        String nom = "Benjamin Joinvil";
        BufferedWriter  bw = null;

        try {
            FileOutputStream fos = openFileOutput("file.txt", Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);// tjrs utiliser car plus efficace
            bw.newLine();
            bw.write(nom);


        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }

    }
    public long questionD(){
        String fileName = "file.txt";
        long caractere =0;
        int c; // retourne est le caractere unicode du caractere lu


        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();


            while (line != null){
                c = br.read();
                if ((char) c == 'c'){
                    caractere++;
                    line = br.readLine();
                }
            }
            qd.setText(String.valueOf(caractere));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caractere;
    }
    public int nbMotScanner(){
        String fileName = "file.txt";
        Scanner sc = null;
        int compteur = 0;

        //Le delimiteur par defaut est un caractere blanc ( espace,\r ,\n etc)
        sc.useDelimiter("\\d"); // changer le delimiteur
        try {
            FileInputStream fis = openFileInput(fileName);
            sc = new Scanner(fis);

            while (sc.hasNext()){
                compteur++;
            }

        }catch (IOException e){
            e.printStackTrace();
        }

            return compteur;

    }



}