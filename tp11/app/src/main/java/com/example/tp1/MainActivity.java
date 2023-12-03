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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Je sais qu'avoir Client ID et URI dans mon instance et dans mon main est une mauvaise pratique,
    // mais si je ne le fais pas de cette manière, les albums vont paraître avec un décalage
    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
    private static final String REDIRECT_URI = "com.example.tp1://callback";


    private SpotifyAppRemote mSpotifyAppRemote;
    final PlayerState[] currentPlayerState = new PlayerState[1];
    ActivityResultLauncher<Intent> lanceur;
    ArrayList<Playliste> playlistes;
    private static Spotify instanceSpotify;
    TextView playliste,nomChanson,nomArtiste,tempsTxt;
    ImageView albumCover, Lien, changePlaylistBtn, playPauseBtn, skipNextBtn, skipPreviousBtn, shuffleBtn, repeatBtn;
    SeekBar tempsSeekBar;
    Chronometer chronometer;
    Boolean isFirstStart;
    long timeWhenStopped;
    int choixPlayliste;



    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des éléments de l'interface utilisateur
        playliste = findViewById(R.id.Playliste);
        nomChanson = findViewById(R.id.nomChanson);
        nomArtiste = findViewById(R.id.nomArtiste);
        albumCover = findViewById(R.id.CoverAlbum);
        Lien = findViewById(R.id.info);
        changePlaylistBtn = findViewById(R.id.changerPlayliste);
        playPauseBtn = findViewById(R.id.PlayPause);
        skipNextBtn = findViewById(R.id.next);
        skipPreviousBtn = findViewById(R.id.Previous);
        shuffleBtn = findViewById(R.id.shuffleButton);
        repeatBtn = findViewById(R.id.repeatButton);
        tempsSeekBar = findViewById(R.id.progressBar);
        chronometer = findViewById(R.id.chronometer);
        tempsTxt = findViewById(R.id.tempsTxt);

        // Initialisation de l'instance Spotify
        instanceSpotify = Spotify.getInstance(this);

        // Configuration de la couleur de la barre de progression
        tempsSeekBar.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        // Initialisation des playlists
        playlistes = new ArrayList<Playliste>();
        playlistes.add(new Playliste("Playliste de Benjamin", "1Q6ivYwu0sg0DEwHr92Jtf"));
        playlistes.add(new Playliste("Kompa mix 2023", "0Ws3ZQf1kxoWbWzUy11yW8"));
        playlistes.add(new Playliste("Kompa HT", "05iajAwuNfjbR0OESaObd04"));
        playlistes.add(new Playliste("ayiti Clasik", "5cLHNE7qPjDU7WMYfWK84f"));
        playlistes.add(new Playliste("Gouyad experience", "2fiuw1m0I5EeKQHSkJK44j"));
        playlistes.add(new Playliste("Haitiano", "0tylu5X3Zhk9iFQWd1qvgX"));
        playlistes.add(new Playliste("Kompa Ayisyen", "12iEmuhY344vOIlqVFEwhV"));


        isFirstStart = true;
        timeWhenStopped = 0;
        choixPlayliste = 0;

        // Initialisation du lanceur d'activités
        lanceur = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        choixPlayliste = result.getData().getIntExtra("choixPlayliste", -1);
                        serealization();
                        playliste.setText(playlistes.get(choixPlayliste).nom);

                        // Pause Spotify avec un délai pour éviter des problèmes de mise à jour
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                instanceSpotify.pause();
                                stopChronometer();
                                tempsTxt.setText(millisToTime(instanceSpotify.getLenght()));
                            }
                        }, 500);
                    }
                }
        );

        // Restauration de la playliste sélectionnée depuis la sérialisation
        FileInputStream fis = null;
        try {
            fis = openFileInput("choixPlayliste.ser");
            // Read or perform operations with the file
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Gestion du clic sur le lien
        playliste.setText(playlistes.get(choixPlayliste).nom);

        Lien.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.masterclass.com/articles/kompa-music-guide"));
            startActivity(i);
        });

        // Gestion du changement de playliste
        changePlaylistBtn.setOnClickListener(source -> {
            Intent i = new Intent(this, ChoisirPlaylistActivity.class);
            lanceur.launch(i);

        });

        // Gestion du clic sur le bouton play/pause
        playPauseBtn.setOnClickListener(source -> {
            if (instanceSpotify.isPlaying()) {
                chronometer.stop();
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                playPauseBtn.setImageResource(R.drawable.play_button_img);
                instanceSpotify.pause();
                stopChronometer();
            } else {
                chronometer.start();
                playPauseBtn.setImageResource(R.drawable.pause_button_img);
                if(!isFirstStart) {
                    instanceSpotify.resume();
                }else {
                    instanceSpotify.play();
                }
            }
        });

        // Gestion du clic sur le bouton suivant
        skipNextBtn.setOnClickListener(source -> {
            instanceSpotify.next();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        });

        // Gestion du clic sur le bouton précédent
        skipPreviousBtn.setOnClickListener(source -> {
            instanceSpotify.previous();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playPauseBtn.setImageResource(R.drawable.pause_button_img);
        });

        // Gestion de la mise à jour du chronomètre
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometer.setOnChronometerTickListener(null);
                update();
                chronometer.setOnChronometerTickListener(this);
            }
        });

        // Gestion du clic sur le bouton de mélange
        shuffleBtn.setOnClickListener(source ->{
            instanceSpotify.toggleShuffle();
        });

        // Gestion du clic sur le bouton de répétition
        repeatBtn.setOnClickListener(source ->{
            instanceSpotify.toggleRepeat();
        });


        // Gestion de la position de la barre de progression
        tempsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                instanceSpotify.slide(seekBar.getProgress());
                chronometer.setBase(SystemClock.elapsedRealtime() - instanceSpotify.getProgress());
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Gestion du résultat de l'activité de choix de playliste
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK) {
            choixPlayliste = data.getIntExtra("choixPlayliste", -1);


            if (choixPlayliste != instanceSpotify.getCurrentPlaylistIndex()) {

                instanceSpotify.setCurrentPlaylist("spotify:playlist:" + playlistes.get(choixPlayliste).ChansonId.toString());
            }
        }
    }

    // Conversion de millisecondes en format heure:minute
    //https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
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
        // Connexion à Spotify
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


    // Méthode de sérialisation
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

    // Méthode de connexion à Spotify
    private void connect() {
        // Abonnement aux mises à jour de l'état du lecteur Spotify
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if (playerState.track == null) return;

                    if (currentPlayerState[0] == null ||
                            !playerState.track.uri.equals(currentPlayerState[0].track.uri))
                        updateChanson(playerState);
                });
        // Abonnement aux mises à jour de l'état du lecteur Spotify (sans traitement)
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {

                });
    }

    // Méthode de mise à jour de l'interface utilisateur
    private void update() {
        if(instanceSpotify.isConnected()) {
            instanceSpotify.update();
            if(instanceSpotify.isPlaying()) {
                if(instanceSpotify.isSongChanged()) {
                    instanceSpotify.resetSongChanged();
                }
                tempsSeekBar.setProgress((int) instanceSpotify.getProgress());
            } else {
                fakeOnStart();
            }
        }
    }

    // Méthode de mise à jour des informations sur la chanson en cours
    private void updateChanson(PlayerState playerState){
        mSpotifyAppRemote.getImagesApi()
                .getImage(playerState.track.imageUri)
                .setResultCallback(bitmap -> {
                    albumCover.setImageBitmap(bitmap);
                    tempsSeekBar.setMax((int) instanceSpotify.getLenght());
                    chronometer.setBase(SystemClock.elapsedRealtime());
                });
        nomChanson.setText(playerState.track.name);
        nomArtiste.setText(playerState.track.artist.name);

        timeWhenStopped = playerState.playbackPosition;
        currentPlayerState[0] = playerState;

        startChronometer();
        if(isFirstStart) {
            instanceSpotify.pause();
            stopChronometer();
            isFirstStart = !isFirstStart;
        }
    }

    // Méthode de démarrage du chronomètre
    private void startChronometer(){
        long songDuration = currentPlayerState[0].track.duration;
        chronometer.setBase(SystemClock.elapsedRealtime() + songDuration - timeWhenStopped);
        chronometer.start();
    }

    // Méthode d'arrêt du chronomètre
    private void stopChronometer(){
        long songDuration = currentPlayerState[0].track.duration;
        timeWhenStopped = SystemClock.elapsedRealtime() + songDuration - chronometer.getBase() ;
        chronometer.stop();
    }

    // Méthode pour simuler le démarrage lorsqu'il n'y a pas de lecture en cours
    private void fakeOnStart() {
        tempsSeekBar.setMax((int) instanceSpotify.getLenght());
        chronometer.setBase(SystemClock.elapsedRealtime() - instanceSpotify.getProgress());
        tempsSeekBar.setProgress((int) instanceSpotify.getProgress());
    }

}