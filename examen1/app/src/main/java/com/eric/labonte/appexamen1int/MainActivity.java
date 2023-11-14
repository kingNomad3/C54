package com.eric.labonte.appexamen1int;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Spinner listeType, listeLits;
    TextView texteType, texteLits;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listeType = findViewById(R.id.listeType);
        listeLits = findViewById(R.id.listeLits);
        texteType = findViewById(R.id.texteType);
        texteLits = findViewById(R.id.texteLits);

        Vector<String> types = new Vector<>();
        types.add("chambre standard");
        types.add("chambre supérieure");
        types.add("suite");

        Vector<String> lits = new Vector();
        lits.add("1 lit");
        lits.add("2 lits");


        ArrayAdapter typeAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, types);
        ArrayAdapter litAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lits);

        listeType.setAdapter(typeAdapt);
        listeLits.setAdapter(litAdapt);

//        try{
//            ObjectInputStream ios= null;
//            FileInputStream fos = openFileInput("fichier.ser");
//
//            ios = new ObjectInputStream(fos);
//
//            types = (Vector<String>) ios.readObject();
//            lits = (Vector<String>) ios.readObject();
//
////
//
//        }catch (ClassNotFoundException | IOException fnfe){
//            fnfe.printStackTrace();
//        }

        Ecouteur ec = new Ecouteur();
        listeType.setOnItemSelectedListener(ec);
        listeLits.setOnItemSelectedListener(ec);


    }

    private class Ecouteur implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String temps = String.valueOf(listeLits.getSelectedItemId());
//            String temos1 = String.valueOf(listeLits[position]);


            texteType.setText(temps);
            texteLits.setText(String.valueOf(listeType));



        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }



    }



    @Override
    protected void onStop() {
        super.onStop();

        try(
                FileOutputStream fos = openFileOutput("fichier.ser",Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos)){

            oos.writeObject(texteType.getText().toString());
            oos.writeObject(texteLits.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}