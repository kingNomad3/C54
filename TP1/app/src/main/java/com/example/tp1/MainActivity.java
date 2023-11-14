package com.example.tp1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
    private static final String REDIRECT_URI = "com.example.tp1://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    final PlayerState[] currentPlayerState = new PlayerState[1];

    ActivityResultLauncher<Intent> lanceur;

    ArrayList<Playlist> playlists;

    TextView playlist, song, artist;
    ImageView cover;
    ImageView getInfoBtn, changePlaylistBtn, playPauseBtn, skipNextBtn, skipPreviousBtn;
    ProgressBar progressBar;
    Chronometer chronometer;

    Boolean isPlaying, isFirstStart;
    long timeWhenStopped;
    int chosenPlaylist;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
//        playlist = findViewById(R.id);
        song = findViewById(R.id.chansonTexte);
        artist = findViewById(R.id.artisteText);
        cover = findViewById(R.id.AlbumImage);
//        getInfoBtn = findViewById(R.id.Info);
//        changePlaylistBtn = findViewById(R.id.skipButton);
        playPauseBtn = findViewById(R.id.playButton);
        skipNextBtn = findViewById(R.id.backButton);
        skipPreviousBtn = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.SeekBarTemps);
//        chronometer = findViewById(R.id.chronometer);

        // mettre le progressbar en rouge
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        // playlists
        playlists = new ArrayList<Playlist>();
        playlists.add(new Playlist("E", "5e2rrc0fWkCLCOeV3q2c5C"));
        playlists.add(new Playlist("P", "37i9dQZF1EIfFiIpBngZoF"));
        playlists.add(new Playlist("M", "76idAjxGH6CK6NmjMcaAAv"));
        playlists.add(new Playlist("R", "1vb24BVDNiaDJjvsRbk8XJ"));
        playlists.add(new Playlist("R", "5A8I08NR69Wnn4LC2Kp1E4"));
        playlists.add(new Playlist("", "2CZSUu4eiRdYjMN1GJpqFR"));

        // lanceur
        lanceur = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        chosenPlaylist = result.getData().getIntExtra("chosenPlaylist", -1);
                        savePlaylist();
                        playlist.setText(playlists.get(chosenPlaylist).nom);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pause();
                                playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                                isPlaying = false;
                            }
                        }, 500);
                    }
                }
        );

        // variables
         isPlaying = false;
        isFirstStart = true;
        timeWhenStopped = 0;
        chosenPlaylist = 0;

        // on cherche le playlist choisi
        try {
            FileInputStream fis = openFileInput("chosenPlaylist.ser");
            ObjectInputStream ois  = new ObjectInputStream(fis);
            chosenPlaylist = (int)ois.readObject();

        } catch (Exception e) {
            savePlaylist();
        }
//        playlist.setText(playlists.get(chosenPlaylist).nom);

        // écouteurs
//        getInfoBtn.setOnClickListener(source -> {
//            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://voyage.caaquebec.com/fr/destinations/afrique/egypte/"));
//            startActivity(i);
//        });

//        changePlaylistBtn.setOnClickListener(v -> {
//            Intent i = new Intent(this, ChoisirPlaylistActivity.class);
//            lanceur.launch(i);
//        });

        playPauseBtn.setOnClickListener(view -> {
            if(!isPlaying) {
                ((ImageButton)view).setImageResource(android.R.drawable.ic_media_pause);
                play(chosenPlaylist);
            }
            else {
                ((ImageButton)view).setImageResource(android.R.drawable.ic_media_play);
                pause();
            }
            isPlaying = !isPlaying;
        });

        skipNextBtn.setOnClickListener(view -> {
            skipNext();
        });

        skipPreviousBtn.setOnClickListener(view -> {
            skipPrevious();
        });

//        chronometer.setOnChronometerTickListener(chrono -> {
//            long songDuration = currentPlayerState[0].track.duration;
//            long elapsedTime = SystemClock.elapsedRealtime() + songDuration - chrono.getBase();
//            int progress = (int) (100 * elapsedTime / songDuration);
//            progressBar.setProgress(progress);
//        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void savePlaylist(){
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = openFileOutput("chosenPlaylist.ser", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(chosenPlaylist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connected() {
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if(playerState.track == null) return;

                    // si c'est la première chanson ou
                    // si c'est la chanson a changé
                    if (currentPlayerState[0] == null ||
                            !playerState.track.uri.equals(currentPlayerState[0].track.uri))
                        updateSong(playerState);
                });
    }

    private void updateSong(PlayerState playerState){
        mSpotifyAppRemote.getImagesApi()
                .getImage(playerState.track.imageUri)
                .setResultCallback(bitmap -> {
                    cover.setImageBitmap(bitmap);
                });
        song.setText(playerState.track.name);
        artist.setText(playerState.track.artist.name);

        timeWhenStopped = playerState.playbackPosition;
        currentPlayerState[0] = playerState;

        startChronometer();
        if(isFirstStart) {pause(); isFirstStart = !isFirstStart;}
    }

    private void startChronometer(){
        long songDuration = currentPlayerState[0].track.duration;
        chronometer.setBase(SystemClock.elapsedRealtime() + songDuration - timeWhenStopped);
        chronometer.start();
    }

    private void stopChronometer(){
        long songDuration = currentPlayerState[0].track.duration;
        timeWhenStopped = SystemClock.elapsedRealtime() + songDuration - chronometer.getBase() ;
        chronometer.stop();
    }

    private void play(int chosenPlaylist) {
        startChronometer();
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + playlists.get(chosenPlaylist).id.toString());
    }

    private void pause() {
        mSpotifyAppRemote.getPlayerApi().pause();
        stopChronometer();
    }

    private void skipNext() {
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    private void skipPrevious() {
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }
}