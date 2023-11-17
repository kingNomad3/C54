package com.eric.accompagnementatelier2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView texteOeuvres, texteArtistes;
    Button bouton;

    Singleton liste;
    Oeuvre oeuvre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texteArtistes = findViewById(R.id.texteArtistes);
        texteOeuvres = findViewById(R.id.texteOeuvres);
        bouton = findViewById(R.id.bouton);







        // 1, utiliser la méthode forEach de la classe ArrayList pour afficher les noms d'artistes présents dans le Singleton, séparés d'une virgule

        List<Oeuvre> afficherListe = liste.getListe();
        afficherListe.forEach(oeuvre1 -> texteOeuvres.setText((CharSequence) oeuvre));



        // 3. utiliser une expression lambda afin de gérer le clic de bouton et d'afficher dans le textView de le nom
        // des oeuvres ayabt été peintes après 1900

//        bouton.setOnClickListener(source->texteArtistes.setText() );


    }
}