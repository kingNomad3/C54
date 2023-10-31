package com.eric.labonte.annexe2importancebuffer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    Button bouton;
    TextView texteDuree, texteNom;
    ActivityResultLauncher<Intent> lanceur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bouton = findViewById(R.id.bouton);
        texteDuree = findViewById(R.id.texteDuree);
        texteNom = findViewById(R.id.texteNom);
        Ecouteur ec = new Ecouteur();
        bouton.setOnClickListener(ec);

        // création du lanceur de boomerang, objet sera appelé au retour du boomerang dans cette classe
        //call back un est rappel automatique suite a un evenement
        lanceur = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new CallBackMusic());
    }


    private class CallBackMusic implements ActivityResultCallback<ActivityResult> {

        // appelé quand je reviens du choix de fichiers sur le téléphone dans cette activité, retour du boomerang
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getData() != null) {
                //un uri est un identifiant unique du fichier selectionner
                //une fois data pour le intente le duexiem data ce que le intente retourne ( le fichier selectionnerO
                Uri uri = result.getData().getData(); // données retournées par l'Intent
                ContentResolver resolver = getContentResolver();// objet permettant d'accéder aux données sur le téléphones ( méthodes CRUD ), présente les données sous forme de tables

                //nom du fichier, comme si c'etais une base de donner
                Cursor cursor = resolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                cursor.moveToFirst();
                texteNom.setText(cursor.getString(0));

                try {
                    // ouvrir un flux de données vers l'URI choisi
                    InputStream stream = resolver.openInputStream(uri); //c'est un stream d'octets

                    texteDuree.setText("durée : " + tempsDeLecture(stream));
                } catch (Exception fnf) {
                    fnf.printStackTrace();
                }
            }
        }
    }


    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            rechercherFichiers();
        }
    }

    public void rechercherFichiers() {
        // intent vers le téléphone
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //filtre
        intent.setType("audio/*");  // ou text/* // fichiers musciaux
        lanceur.launch(intent);  // lance l'intent différemment qu'avec startActivity car on attend un résultat dans cette activité, affiche les fichiers musicaux
    }

    // EXception ou IOException
    public String tempsDeLecture(InputStream chemin) throws Exception {
        long temps =0;
        //on lit des octets alors input stream
        FileInputStream fis = (FileInputStream) chemin;
        BufferedInputStream bis = new BufferedInputStream(fis);
        long debut = System.currentTimeMillis();

        //va juste lire les donnes
        //avec buffer
        while (bis.read()!= -1);
        //sans buffer
//        while (fis.read()!= -1);
        long fin = System.currentTimeMillis();
        temps= fin -debut;

        return String.valueOf(temps);
    }

}