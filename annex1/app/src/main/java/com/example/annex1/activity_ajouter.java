package com.example.annex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class activity_ajouter extends AppCompatActivity {
    Button ajouter;
    EditText textAjouter;
    Ecouteur ec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        textAjouter = findViewById(R.id.textAjouter);
        ajouter = findViewById(R.id.ajouterPage);

        ec = new Ecouteur();

        ajouter.setOnClickListener(ec);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ajouterMemo(textAjouter.getText().toString());

            finish();
        }

    }

    public void ajouterMemo(String memo) {

        //on invente un nom de fichier, si n'existe oas va le creer
        try (FileOutputStream fos = openFileOutput("memos.txt", Context.MODE_APPEND);//flux de communication
             OutputStreamWriter osw = new OutputStreamWriter(fos); //flux de traduction
             BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write(memo);
            bw.newLine();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
              e.printStackTrace();
         }


    }


}

//    public void ajouterMemo(String memo) {
//        FileOutputStream fos = null;
//        BufferedWriter bw = null; // rend plus rapid
//
//        //on invente un nom de fichier, si n'existe oas va le creer
//        try {
//
//            fos = openFileOutput("memos.txt", Context.MODE_APPEND);//flux de communication
//            OutputStreamWriter osw = new OutputStreamWriter(fos); //flux de traduction
//            bw = new BufferedWriter(osw);
//            bw.write(memo);
//            bw.newLine();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            fermerFlux();
//        }

//    public void fermerFlux(Writer bw) {
//        try {
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    }
