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
import java.util.List;
import java.util.Map;

public class ChoisirPlaylistActivity extends AppCompatActivity {

    ListView liste;
    SimpleAdapter adapter;
    List<Map<String, ?>> playlistes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_playlist);

        playlistes = new ArrayList<>();

        playlistes.add(createPlaylistMap("Playliste de Benjamin", "1Q6ivYwu0sg0DEwHr92Jtf"));
        playlistes.add(createPlaylistMap("Kompa mix 2023", "0Ws3ZQf1kxoWbWzUy11yW8"));
        playlistes.add(createPlaylistMap("Kompa HT", "05iajAwuNfjbR0OESaObd04"));
        playlistes.add(createPlaylistMap("ayiti Clasik", "5cLHNE7qPjDU7WMYfWK84f"));
        playlistes.add(createPlaylistMap("Gouyad experience", "2fiuw1m0I5EeKQHSkJK44j"));
        playlistes.add(createPlaylistMap("Haitiano", "0tylu5X3Zhk9iFQWd1qvgX"));
        playlistes.add(createPlaylistMap("Kompa Ayisyen", "12iEmuhY344vOIlqVFEwhV"));

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


    private Map<String, String> createPlaylistMap(String playlistName, String playlistId) {
        Map<String, String> playlistMap = new HashMap<>();
        playlistMap.put("playlistName", playlistName);
        playlistMap.put("playlistId", playlistId);
        return playlistMap;
    }


}