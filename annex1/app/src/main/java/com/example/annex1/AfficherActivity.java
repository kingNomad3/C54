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

    //    GestionBD bd;
    ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher);

        liste = findViewById(R.id.liste);
        ArrayAdapter adapteur = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recupererMemosFichiers());
        liste.setAdapter(adapteur);
    }

    public ArrayList<String> recupererMemosFichiers() {
        String fileName = "memos.txt";
        ArrayList<String> temp = new ArrayList<>();


        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while (line != null){
                temp.add(line);
                line = br.readLine();
//                System.out.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



        return temp;
    }

//    String line
//    While((line = br.readLine()) !=null){
//        temp.add(line);
//    }

//    while(br.ready())}
//        string line = br.readline()
//        temp.add(ligne)
//     {


}