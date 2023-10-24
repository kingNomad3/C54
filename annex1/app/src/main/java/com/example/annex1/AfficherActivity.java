package com.example.annex1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AfficherActivity extends AppCompatActivity {
    GestionBD bd;
    ListView ajouterPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher);
    }

    @Override
    protected void onStart() {
        super.onStart();



//        ArrayAdapter adapteur = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bd.retournNote());

        ajouterPage.setAdapter(adapteur);
    }
}