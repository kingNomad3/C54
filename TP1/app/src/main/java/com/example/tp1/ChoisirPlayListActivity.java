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
import java.util.Hashtable;
import java.util.Vector;

public class ChoisirPlayListActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_play_list);

        playlists = new ArrayList<String>();
        playlists.add(" rap");
        playlists.add(" rap");
        playlists.add(" rap");
        playlists.add(" rap");
        playlists.add(" rap");
        playlists.add(" rap");


        list = findViewById(R.id.liste);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_choisir_play_list, playlists);

        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent();
            i.putExtra("chosenPlaylist", position);
            setResult(Activity.RESULT_OK, i);
            finish();
        });
    }

}