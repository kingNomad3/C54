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
    Vector<HashMap<String, Object>> hmPlaylists;
    ListView list;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist1 = new Playliste(R.drawable.playlist1, "Jazz Instrumental Classics", "misc.",
                "How does one write something catchy to describe a set of strandards?",
                "63 songs", "6h37m", "spotify:playlist:1dHJUpqHMRbGN3kpMJDw0T");

        playlist2 = new Playliste(R.drawable.playlist2, "Chet Baker Sings", "Chet Baker",
                "The Prince of Cool",
                "14 songs", "44m21s", "spotify:album:5JJ779nrbHx0KB2lBrMMa4");

        playlist3 = new Playliste(R.drawable.playlist3, "Kind of Blue", "Miles Davis",
                "among the most influential and acclaimed figures in the history of jazz",
                "5 songs", "45m45s", "spotify:album:1weenld61qoidwYuZ1GESA");



        playlists = new Playliste[]{playlist1, playlist2, playlist3, playlist4, playlist5, playlist6, playlist7,playlist8};

        hmPlaylists = new Vector<>();
        for (Playliste playlist:playlists) {
            hmPlaylists.add(
                    new HashMap(){{
                        put("cover",playlist.imageCover);
                        put("name", playlist.nomChanson);
                        put("artist", playlist.nomArtiste);
                        put("description", playlist.description);
                        put("nbSongs", playlist.nbSongs);
                        put("duration", playlist.duree);
                        put("link", playlist.link);
                    }}
            );
        }

        simpleAdapter = new SimpleAdapter(
                this,
                hmPlaylists,
                R.layout.playliste,
                new String[]{"cover", "name", "artist", "description", "nbSongs", "duration"},
                new int[]{R.id.cover, R.id.nameText, R.id.artistText, R.id.descriptionText, R.id.tracksText, R.id.durationText}
        );

//        list = findViewById(R.id.list);
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener((adapterView, view, pos, l) -> {
            intent = new Intent();
            pickedPlaylist = (String) hmPlaylists.get(pos).get("link");
            intent.putExtra("link", pickedPlaylist);
            setResult(RESULT_OK, intent);
            finish();
        });

    }
}