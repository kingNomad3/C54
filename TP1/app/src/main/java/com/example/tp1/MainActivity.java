package com.example.tp1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    private static Spotify instance;
    private ImageView boutonPlay;
    private TextView nomChanson;
    private TextView nomArtiste;
    private ImageView imageChanson;
    private SeekBar tempsChanson;
    private Chronometer chrono;
    private ImageView skipBack;
    private ImageView skipForward;
    private long tempsPause = 0;

    private Button pagePlaylists;
    private androidx.activity.result.ActivityResultLauncher<Intent> launcher;
    private boolean reload = true; //Est true li il y as besoin de mettre a jour les infos de la chanson

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = Spotify.getInstance(this);
        boutonPlay = findViewById(R.id.playButton);
        nomChanson = findViewById(R.id.nomChanson);
        nomArtiste = findViewById(R.id.artisteText);
        imageChanson = findViewById(R.id.AlbumImage);
        tempsChanson = findViewById(R.id.SeekBarTemps);
        chrono = findViewById(R.id.chrono);
        skipBack = findViewById(R.id.backButton);
        skipForward = findViewById(R.id.skipButton);
        pagePlaylists = findViewById(R.id.pagePlaylists);


        updateInfo(); //Afin de faire que onChronometerTick commence à être appelé

        //Ticks
        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                //Empêche onChronometerTick d'être appelé avant que updateInfo est fini au cas ou il faut changer la valeur du chrono
                chronometer.setOnChronometerTickListener(null);
                updateInfo();
                chronometer.setOnChronometerTickListener(this);
            }
        });
        //Play et pause
        boutonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instance.isPlaying()){
                    pause();
                }
                else{
                    play();
                }
            }
        });

        //Changement chanson
        skipBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instance != null) {
                    instance.previousSong();
                } else {
                    // Handle the case where the instance is null (perhaps show an error message)
                    Log.e("MainActivity", "Instance of Spotify is null");
                }
            }
        });
        skipForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.nextSong();
            }
        });
        //Mouvement barre progress
        tempsChanson.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                instance. move(seekBar.getProgress());
                seekBar.setProgress((int) instance.getSongProgress());
            }
        });



        //Partie boomerang
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    instance.setActivePlaylist((String) result.getData().getSerializableExtra("lienSpotify"));
                }
            }
        });
        pagePlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new Intent(MainActivity.this, ChoisirPlayListActivity.class));
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (instance.isConnected()){
            reload = true;
            play();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        pause();
    }
    private void play() {
        instance.play();
        //chrono.setBase(SystemClock.elapsedRealtime() + tempsPause);
        chrono.setBase(SystemClock.elapsedRealtime() - instance.getSongProgress());
        chrono.start();
//        boutonPlay.setImageDrawable(getDrawable(R.drawable.pause));
    }
    private void pause() {
        instance.pause();
        chrono.stop();
        //tempsPause = chrono.getBase() - SystemClock.elapsedRealtime();
//        boutonPlay.setImageDrawable(getDrawable(R.drawable.play));

    }
    private void updateInfo(){
        if (instance.isConnected()){
            instance.updateInfo();
            if (instance.songChanged() || reload == true){ //Verfie si la chanson as été changée il y as peu
                newSong();
                reload = false;
                instance.resetSongChanged(); //Dit a SpotifyDiffuseur que le changement de chanson à été pris en compte
            }
            updateSeekBar(); //Ajoute la seconde qui est passée
        }
    }
    private void updateSeekBar(){
        tempsChanson.setProgress(tempsChanson.getProgress() + 1000);
    }
    //Est appelé après un changement de chanson
    private void newSong(){
        nomChanson.setText(instance.getNomChanson());
        nomArtiste.setText(instance.getNomArtiste());
        tempsChanson.setMax((int) instance.getSongLenght());
        //Met pas a zero au cas ou une chanson jouais déja a ouverture de l'app ou changement de playlist
        setChrono();
        tempsChanson.setProgress((int) instance.getSongProgress());
        imageChanson.setImageBitmap(instance.getCouvertureChanson());
    }
    //Met le temps sur le chronometre égal au progress de la chanson
    private void setChrono() {
        chrono.setBase(SystemClock.elapsedRealtime() - instance.getSongProgress());
    }

}
