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

        // Initialisation de l'instance Singleton et de l'instance Oeuvre
        liste = Singleton.getInstance();


        // 1. Utiliser la méthode forEach de la classe ArrayList pour afficher les noms d'artistes
        // présents dans le Singleton, séparés par une virgule
        List<Oeuvre> afficherListe = liste.getListe();


        // 3. Utiliser une expression lambda pour gérer le clic du bouton et afficher dans le TextView
        // les noms des oeuvres ayant été créées après 1900
        bouton.setOnClickListener(source -> {
            List<Oeuvre> oeuvresApres1900 = liste.filtre();
            StringBuilder oeuvresBuilder = new StringBuilder();
            oeuvresApres1900.forEach(oeuvre1 -> oeuvresBuilder.append(oeuvre1.getTitre()).append("\n"));
            String oeuvresString = oeuvresBuilder.toString();

            StringBuilder artistesBuilder = new StringBuilder();
            afficherListe.forEach(oeuvre1 -> artistesBuilder.append(oeuvre1.getNomArtiste()).append("\n"));
            String artistesString = artistesBuilder.toString();
            texteOeuvres.setText(artistesString);

            texteArtistes.setText(oeuvresString);
        });
    }
}