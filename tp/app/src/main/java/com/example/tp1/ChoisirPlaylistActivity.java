package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChoisirPlaylistActivity extends AppCompatActivity {

    ListView liste;
    SimpleAdapter adapter;
    ArrayList<Map<String, String>> playlistes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_playlist);

        playlistes = new ArrayList<>();
        Map<String, String> playlist1 = new HashMap<>();
        playlist1.put("playlistName", "Playlist personelle de Benjamin");

        Map<String, String> playlist2 = new HashMap<>();
        playlist2.put("playlistName", "Kompa mix 2023");

        Map<String, String> playlist3 = new HashMap<>();
        playlist3.put("playlistName", "Kompa HT");

        Map<String, String> playlist4 = new HashMap<>();
        playlist4.put("playlistName", "ayiti Klasik");

        Map<String, String> playlist5 = new HashMap<>();
        playlist5.put("playlistName", "Gouyad experience");

        Map<String, String> playlist6 = new HashMap<>();
        playlist6.put("playlistName", "Haitiano");

        Map<String, String> playlist7 = new HashMap<>();
        playlist7.put("playlistName", "Kompa HT");


        playlistes.add(playlist1);
        playlistes.add(playlist2);
        playlistes.add(playlist4);
        playlistes.add(playlist5);
        playlistes.add(playlist6);
        playlistes.add(playlist7);




        String[] from = {"playlistName"};
        int[] to = {R.id.text1};

        adapter = new SimpleAdapter(
                this,
                playlistes,
                R.layout.liste_textview,
                from,
                to
        );

        liste = findViewById(R.id.liste);
        liste.setAdapter(adapter);

        liste.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent();
            i.putExtra("choixPlayliste", position);
            setResult(Activity.RESULT_OK, i);
            finish();
        });
    }


}