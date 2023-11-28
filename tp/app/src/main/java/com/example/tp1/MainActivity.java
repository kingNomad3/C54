package com.example.tp1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //je sais qu'avoir Client ID et URI dans mon instance et dans mon main est une mauvaise pratice, mais si je ne le fais pas de cette maniere
    // les albumes vont parraitre avec un decalage
    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
    private static final String REDIRECT_URI = "com.example.tp1://callback";


    private SpotifyAppRemote mSpotifyAppRemote;
    final PlayerState[] currentPlayerState = new PlayerState[1];
    ActivityResultLauncher<Intent> lanceur;
    ArrayList<Playliste> playlistes;
    private static Spotify instance;
    TextView playliste,nomChanson,nomArtiste,tempsTxt;
    ImageView albumCover, Lien, changePlaylistBtn, playPauseBtn, skipNextBtn, skipPreviousBtn;
    SeekBar tempsSeekBar;
    Chronometer chronometer;
    Boolean isPlaying, isFirstStart;
    long timeWhenStopped;
    long progress;
    int choixPlayliste;
    private long pauseTemps = 0;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playliste = findViewById(R.id.Playliste);
        nomChanson = findViewById(R.id.nomChanson);
        nomArtiste = findViewById(R.id.nomArtiste);
        albumCover = findViewById(R.id.CoverAlbum);
        Lien = findViewById(R.id.info);
        changePlaylistBtn = findViewById(R.id.changerPlayliste);
        playPauseBtn = findViewById(R.id.PlayPause);
        skipNextBtn = findViewById(R.id.next);
        skipPreviousBtn = findViewById(R.id.Previous);
        tempsSeekBar = findViewById(R.id.progressBar);
        chronometer = findViewById(R.id.chronometer);
        tempsTxt = findViewById(R.id.tempsTxt);


        isPlaying = false;
        isFirstStart = true;
        timeWhenStopped = 0;
        choixPlayliste = 0;

        tempsSeekBar.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        // playlists
        playlistes = new ArrayList<Playliste>();
        playlistes.add(new Playliste("Playliste de Benjamin", "1Q6ivYwu0sg0DEwHr92Jtf"));
        playlistes.add(new Playliste("Kompa mix 2023", "0Ws3ZQf1kxoWbWzUy11yW8"));
        playlistes.add(new Playliste("Kompa HT", "05iajAwuNfjbR0OESaObd04"));
        playlistes.add(new Playliste("ayiti Clasik", "5cLHNE7qPjDU7WMYfWK84f"));
        playlistes.add(new Playliste("Gouyad experience", "2fiuw1m0I5EeKQHSkJK44j"));
        playlistes.add(new Playliste("Haitiano", "0tylu5X3Zhk9iFQWd1qvgX"));
        playlistes.add(new Playliste("Kompa Ayisyen", "0tylu5X3Zhk9iFQWd1qvgX"));



        instance = Spotify.getInstance(this);


        lanceur = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        choixPlayliste = result.getData().getIntExtra("choixPlayliste", -1);
                        serealization();
                        playliste.setText(playlistes.get(choixPlayliste).nom);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pause();
                                playPauseBtn.setImageResource(R.drawable.play_button_img);
                                isPlaying = false;
                            }
                        }, 500);
                    }
                }
        );

        //deseralization
        try {
            FileInputStream fis = openFileInput("choixPlayliste.ser");
            ObjectInputStream ois  = new ObjectInputStream(fis);
            choixPlayliste = (int)ois.readObject();

        } catch (Exception e) {
            serealization();
        }

        playliste.setText(playlistes.get(choixPlayliste).nom);

        Lien.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.masterclass.com/articles/kompa-music-guide"));
            startActivity(i);
        });

        changePlaylistBtn.setOnClickListener(source -> {
            Intent i = new Intent(this, ChoisirPlaylistActivity.class);
            lanceur.launch(i);

//            instance.setCurrentPlaylist("spotify:playlist:" + playlistes.get(choixPlayliste).ChansonId.toString());
//            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        });

        if (isPlaying){
            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        }
        playPauseBtn.setOnClickListener(source -> {
            if (!isPlaying) {
                if (timeWhenStopped == 0) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                }
                chronometer.start();
                playPauseBtn.setImageResource(R.drawable.pause_button_img);
                play(choixPlayliste);
            } else {
                chronometer.stop();
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                playPauseBtn.setImageResource(R.drawable.play_button_img);
                pause();
            }
            isPlaying = !isPlaying;
        });

        skipNextBtn.setOnClickListener(source -> {
            instance.next();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        });

        skipPreviousBtn.setOnClickListener(source -> {
            instance.previous();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometer.setOnChronometerTickListener(null);
                update();
                chronometer.setOnChronometerTickListener(this);
            }
        });

        tempsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                instance.slide(seekBar.getProgress());
                chronometer.setBase(SystemClock.elapsedRealtime() - instance.getProgress());
            }
            //useless:
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK) {
            choixPlayliste = data.getIntExtra("choixPlayliste", -1);

            // Check if the selected playlist is different from the current one
            if (choixPlayliste != instance.getCurrentPlaylistIndex()) {
                // Start playing the first song of the new playlist
                instance.setCurrentPlaylist("spotify:playlist:" + playlistes.get(choixPlayliste).ChansonId.toString());
            }
        }
    }
    private String millisToTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
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
                        connect();
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

    private void serealization(){
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = openFileOutput("choixPlayliste.ser", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(choixPlayliste);
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

    private void connect() {
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if (playerState.track == null) return;

                    if (currentPlayerState[0] == null ||
                            !playerState.track.uri.equals(currentPlayerState[0].track.uri))
                        updateSong(playerState);
                });

        // Initialize playerApi
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    // Handle player state updates if needed
                });
    }


    private void update() {
        if(instance.isConnected()) {
            instance.update();
            if(isPlaying) {
                if(instance.isSongChanged()) {

                    instance.resetSongChanged();
                }

                tempsSeekBar.setProgress((int) instance.getProgress());
            } else {
                fakeOnStart();
            }
        }
    }

    private void updateSong(PlayerState playerState){
        mSpotifyAppRemote.getImagesApi()
                .getImage(playerState.track.imageUri)
                .setResultCallback(bitmap -> {
                    albumCover.setImageBitmap(bitmap);
                });
        nomChanson.setText(playerState.track.name);
        nomArtiste.setText(playerState.track.artist.name);

        timeWhenStopped = playerState.playbackPosition;
        currentPlayerState[0] = playerState;

        startChronometer();
        if(isFirstStart) {
            pause();
            isFirstStart = !isFirstStart;
        }
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

    private void play(int choixPlayliste) {
        if (timeWhenStopped == 0) {
            chronometer.setBase(SystemClock.elapsedRealtime());
        }
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + playlistes.get(choixPlayliste).ChansonId.toString());
    }

    private void pause() {
        mSpotifyAppRemote.getPlayerApi().pause();
        stopChronometer();
    }

    private void fakeOnStart() {

        tempsSeekBar.setMax((int) instance.getLenght());
        chronometer.setBase(SystemClock.elapsedRealtime() - instance.getProgress());
        tempsSeekBar.setProgress((int) instance.getProgress());


    }

}