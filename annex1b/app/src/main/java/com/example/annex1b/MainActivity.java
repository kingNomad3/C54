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

public class                                                   MainActivity extends AppCompatActivity {
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
                questionC();
                questionD();
                nbMotScanner();
            }

        }


    }

    public long questionA(){
//        A)	Une méthode retournant le nombre de lignes que compte votre fichier texte
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
//        B)	Une méthode retournant le nombre de caractères que compte votre fichier texteB)	Une méthode retournant le nombre de caractères que compte votre fichier texte
        String fileName = "file.txt";
        long caractere =0;
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null){
                caractere += line.length();
            }
            qb.setText(String.valueOf(caractere));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caractere;
    }



    public long questionC(){
//    C)	Une méthode retournant le nombre de « c » que comprend votre fichier texte
        String fileName = "file.txt";
        long caractere =0;
        int c; // retourne est le caractere unicode du caractere lu


        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            while ((c = br.read()) != -1) { // Lire les caractères jusqu'à la fin du fichier (EOF)
                if ((char) c == 'c') { // Vérifier si le caractère est 'c'
                    caractere++; // Incrémenter le compteur si c'est 'c'
                }
            }

            // Supposons que qd est un composant d'interface utilisateur (TextView) qui doit être mis à jour dans le thread UI.
            qc.setText(String.valueOf(caractere));


        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception, éventuellement informer l'utilisateur
        }
        return caractere; // Retourner le compte de 'c'
    }


    public void questionD(){

        String nom = "Benjamin Joinvil";
        try {
            FileOutputStream fos = openFileOutput("file.txt", Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw); // Toujours utiliser car plus efficace
            bw.newLine(); // Écrire une nouvelle ligne
            bw.write(nom); // Écrire le nom
            bw.flush(); // Assurez-vous que tout est écrit dans le fichier avant de fermer quand metter flush


        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }

    }
    public int nbMotScanner(){
//        trouver le nombre de mots de votre fichier texte en utilisant un Scanner.
        String fileName = "file.txt";
        Scanner sc = null;
        int compteur = 0;


        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String ligne = br.readLine();;

            while (ligne  != null) {
                sc = new Scanner(ligne);
                //Le delimiteur par defaut est un caractere blanc ( espace,\r ,\n etc)
                sc.useDelimiter("\\d"); // changer le delimiteur

                while (sc.hasNext()) {
                    sc.next();
                    compteur++;
                }
              ligne = br.readLine();
            }
            qd.setText(String.valueOf(compteur));
        }catch (IOException e){
            e.printStackTrace();
        }
            return compteur;

    }

}