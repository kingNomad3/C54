package com.example.tp1;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;


//public class MainActivity extends AppCompatActivity {
//
//    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
//    private static final String REDIRECT_URI = "com.example.tp1://callback";
//    private SpotifyAppRemote mSpotifyAppRemote;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d("MainActivity", "Connected! Yay!");
//
//                        // Now you can start interacting with App Remote
//                        connected();
//
//                    }
//
//                    public void onFailure(Throwable throwable) {
//                        Log.e("MyActivity", throwable.getMessage(), throwable);
//
//                        // Something went wrong when attempting to connect! Handle errors here
//                    }
//                });
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//    }
//
//    private void connected() {
//        // Play a playlist
//        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//
//        // Subscribe to PlayerState
//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final Track track = playerState.track;
//                    if (track != null) {
//                        Log.d("MainActivity", track.name + " by " + track.artist.name);
//                    }
//                });
//    }
//}


//package com.example.tp1;

import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
public class MainActivity extends AppCompatActivity {

    private ImageView backChansonButton,searchButton, prochainChansonBoutton,  shuffleButton, repeatButton,coverImage, jouerPauseButton;
    private TextView nomArtisteText, tempsText, nomChansonText, linkText;
    private Chronometer temps;
    private SeekBar timeSeekBar;
    private androidx.activity.result.ActivityResultLauncher<Intent> launcher;
    private static Spotify instance;
    private long pauseTime = 0;
    private boolean started = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        jouerPauseButton = findViewById(R.id.jouerPauseButton);
        prochainChansonBoutton = findViewById(R.id.prochainChansonBoutton);
        backChansonButton = findViewById(R.id.backChansonButton);
        shuffleButton = findViewById(R.id.shuffleButton);
//        repeatButton = findViewById(R.id.repeatButton);

        //infos
        nomChansonText = findViewById(R.id.nomChansonText);
        nomArtisteText = findViewById(R.id.nomArtisteText);
        coverImage = findViewById(R.id.coverImage);

        //time
        tempsText = findViewById(R.id.tempsText);
        temps = findViewById(R.id.textTime);
        timeSeekBar = findViewById(R.id.slider);

        //header
        searchButton = findViewById(R.id.searchButton);
        linkText = findViewById(R.id.linkText);

        instance = Spotify.getInstance(this);


        temps.start();

        //LISTENERS ---------------------------------------------------------

        //PLAY ACTIONS:
        jouerPauseButton.setOnClickListener(v -> {
            if(instance.isPlaying()) {
                instance.pause();
                temps.stop();
                pauseTime = temps.getBase() - SystemClock.elapsedRealtime();
                jouerPauseButton.setImageResource(R.drawable.play_img);
            } else {
                instance.resume();
                temps.setBase(SystemClock.elapsedRealtime() + pauseTime);
                temps.start();
                jouerPauseButton.setImageResource(R.drawable.pause_img);
            }
        });

        prochainChansonBoutton.setOnClickListener(v -> {
            instance.next();
            temps.setBase(SystemClock.elapsedRealtime());
            jouerPauseButton.setImageResource(R.drawable.ic_pause);
        });

        backChansonButton.setOnClickListener(v -> {
            instance.previous();
            temps.setBase(SystemClock.elapsedRealtime());
            jouerPauseButton.setImageResource(R.drawable.ic_pause);
        });

        shuffleButton.setOnClickListener(v -> instance.toggleShuffle());

//        repeatButton.setOnClickListener(v -> instance.toggleRepeat());

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                instance.slide(seekBar.getProgress());
                temps.setBase(SystemClock.elapsedRealtime() - instance.getProgress());
            }
            //useless:
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
        });

        linkText.setOnClickListener(v -> {
            String url = "https://www.masterclass.com/articles/kompa-music-guide";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK){
                assert result.getData() != null;
                instance.setCurrentPlaylist((String) result.getData().getSerializableExtra("link"));
                infoChansonAff(); //problem: doesnt work if first thing done when app launch, idk
            }
        });
        searchButton.setOnClickListener(v -> launcher.launch(new Intent(MainActivity.this, PlaylistActivity.class)));

        temps.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometer.setOnChronometerTickListener(null);
                update();
                chronometer.setOnChronometerTickListener(this);
            }
        });
    }

    private void update() {
        if(instance.isConnected()) {
            instance.update();
            if(started) {
                if(instance.isSongChanged()) {
                    playNew();
                    instance.resetSongChanged();
                }

                timeSeekBar.setProgress((int) instance.getProgress());
            } else {
                fakeOnStart();
            }
        }
    }

    private void playNew() {
        infoChansonAff();
        timeSeekBar.setMax((int) instance.getLenght());
        temps.setBase(SystemClock.elapsedRealtime()); //reset timer
    }

    private void infoChansonAff() {
        nomChansonText.setText(instance.getName());
        nomArtisteText.setText(instance.getArtist());
        coverImage.setImageBitmap(instance.getCover());
        tempsText.setText(millisToTime(instance.getLenght()));
    }

    @SuppressLint("DefaultLocale")
    private String millisToTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void fakeOnStart() {
        infoChansonAff();
        timeSeekBar.setMax((int) instance.getLenght());
        temps.setBase(SystemClock.elapsedRealtime() - instance.getProgress());
        timeSeekBar.setProgress((int) instance.getProgress());

        if(instance.isPlaying()) {
            jouerPauseButton.setImageResource(R.drawable.ic_pause);

        } else { temps.stop(); }
        started = true;
    }
}
//public class MainActivity extends AppCompatActivity {
//
//    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
//    private static final String REDIRECT_URI = "com.example.tp1://callback";
//    private SpotifyAppRemote mSpotifyAppRemote;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d("MainActivity", "Connected! Yay!");
//
//                        // Now you can start interacting with App Remote
//                        connected();
//
//                    }
//
//                    public void onFailure(Throwable throwable) {
//                        Log.e("MyActivity", throwable.getMessage(), throwable);
//
//                        // Something went wrong when attempting to connect! Handle errors here
//                    }
//                });
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//    }
////                                  ~                     ~
////https://open.spotify.com/playlist/4WGFTe1rlq8oVvsj3zyCL7?si=RSQLn7D_Smi3hgG9ybu_5w
//    //https://open.spotify.com/track/4s0pYq8RcXXGVDibcV9p58?si=df10e31b08c240ce
//    private void connected() {
//        // Play a playlist
//        mSpotifyAppRemote.getPlayerApi().play("spotify:track:4s0pYq8RcXXGVDibcV9p58");
//
//        // Subscribe to PlayerState
//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final Track track = playerState.track;
//                    if (track != null) {
//                        Log.d("MainActivity", track.name + " by " + track.artist.name);
//                    }
//                });
//    }
//}

