package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.Vector;

public class ChoisirPlayListActivity extends AppCompatActivity {

    ListView listeDePlayListes;
    Ecouteur ec;
    Intent intent;
    String choix;
    SimpleAdapter adapteurListe;

    Playliste playliste1;
    Playliste playliste2;
    Playliste playliste3;

    Playliste[] playlistes;
    Vector<HashMap<String, Object>> infoPlayListes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_play_list);

        listeDePlayListes = findViewById(R.id.liste);
        ec = new Ecouteur();

        //Si il faut rajouter une playlist just créer un objet et le mettre dans playlists
        playliste1 = new Playliste("Lobey", 180, 50, R.drawable.playlist1, "55456");
        playliste2 = new Playliste("Gettho girl whine", 130, 37, R.drawable.playlist2, "4121");
        playliste3 = new Playliste("sugar and spice",55,15, R.drawable.playlist3, "3545345");


        playlistes = new Playliste[]{playliste1, playliste2, playliste3};


        infoPlayListes = new Vector<>();
        for (Playliste playliste : playlistes) {
            infoPlayListes.add(
                    new HashMap(){{
                        put("nom", playliste.nom);
                        put("duree", playliste.duree);
                        put("nbChansons", playliste.nbChansons);
                        put("image", playliste.image);
                        put("lienSpotify", playliste.lienSpotify);

                    }}
            );
        }

        adapteurListe = new SimpleAdapter(
                this,
                infoPlayListes,
                R.layout.activity_choisir_play_list,
                new String[]{"name", "duree", "nbTracks", "image"},
                new int[]{R.id.chansonTexte, R.id.duréePlaylist, R.id.nbChansons, R.id.imagePlaylist}
        );

        listeDePlayListes.setAdapter(adapteurListe);
        listeDePlayListes.setOnItemClickListener(ec);
    }
    private class Ecouteur implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent();
            choix = (String) infoPlayListes.get(position).get("lienSpotify");
            intent.putExtra("lienSpotify", choix);
            setResult(RESULT_OK, intent);
            finish();

        }
    }

}