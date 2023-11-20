package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.Vector;

public class PlaylistActivity extends AppCompatActivity {

    Playliste playlist1, playlist2, playlist3, playlist4, playlist5, playlist6, playlist7, playlist8;
    Playliste[] playlists;
    String pickedPlaylist;
    SimpleAdapter simpleAdapter;
    Vector<HashMap<String, Object>> listePlayliste;
    ListView list;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playliste);
        https://open.spotify.com/playlist/1Q6ivYwu0sg0DEwHr92Jtf?si=7bd92ae7b00949b1
        playlist1 = new Playliste(R.drawable.playlist1, "kompa" +
                "Playliste personelle de Benjamin", "Benjamin",
                "Ceci est ma playliste personelle ",
                " 28 chanson", "1h52m", "spotify:playlist:1Q6ivYwu0sg0DEwHr92Jtf");

        playlist2 = new Playliste(R.drawable.playlist2, "Ctest", "test",
                "test",
                "test", "test", "spotify:album:5JJ779nrbHx0KB2lBrMMa4");

        playlist2 = new Playliste(R.drawable.playlist3, "Ctest", "test",
                "test",
                "test", "test", "spotify:album:5JJ779nrbHx0KB2lBrMMa4");



        playlists = new Playliste[]{playlist1, playlist2, playlist3};

        listePlayliste = new Vector<>();
        for (Playliste playlist:playlists) {
            listePlayliste.add(
                    new HashMap(){{
                        put("cover",playlist.imageCover);
                        put("name", playlist.nomChanson);
                        put("artist", playlist.nomArtiste);
                        put("description", playlist.description);
                        put("nbSongs", playlist.nbSongs);
                        put("duration", playlist.duree);
                        put("lien", playlist.lien);
                    }}
            );
        }

        simpleAdapter = new SimpleAdapter(
                this,
                listePlayliste,
                R.layout.playliste,
                new String[]{"cover", "name", "artist", "description", "nbSongs", "duration"},
                new int[]{R.id.cover, R.id.nameText, R.id.artistText, R.id.descriptionText, R.id.tracksText, R.id.durationText}
        );

        list = findViewById(R.id.list);
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener((adapterView, view, pos, l) -> {
            intent = new Intent();
            pickedPlaylist = (String) listePlayliste.get(pos).get("lien");
            intent.putExtra("lien", pickedPlaylist);
            intent.putExtra("lien", pickedPlaylist);
            setResult(RESULT_OK, intent);
            finish();
        });

    }
}