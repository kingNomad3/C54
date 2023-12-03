package com.example.tp1;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class Spotify {

    private static Spotify instance;
    private Context context;

    private static final String CLIENT_ID = "6aa9f568231047e1945bf21ade475261";
    private static final String REDIRECT_URI = "com.example.tp1://callback";
    private String currentPlaylist = "spotify:playlist:1Q6ivYwu0sg0DEwHr92Jtf";
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;

    //info player
    private boolean isConnected = false;
    private String currentSongURI;
    private boolean isPlaying;
    private boolean songChanged;
    private String nom;
    private String artiste;
    private Bitmap cover;
    private long lenght;
    private long progress;
    private int currentPlaylistIndex;



    private Spotify(Context c) {
        this.context = c;
        this.connect();
    }

    public static Spotify getInstance(Context c) {
        if(instance == null)
            instance = new Spotify(c);
        return instance;
    }


    public void connect() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams, new Connector.ConnectionListener() {
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                playerApi = mSpotifyAppRemote.getPlayerApi();
                Log.d("Spotify", "Connected to Spotify App Remote");
                connected();
            }

            public void onFailure(Throwable throwable) {
                Log.e("Spotify", "Connection to Spotify App Remote failed: " + throwable.getMessage());
                // Handle failure, for example, by retrying or showing a message to the user
            }
        });
    }

    public void disconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        this.update();
        isConnected = true;
    }


    public void play() {
        if (playerApi != null) {
            playerApi.play(currentPlaylist);
            isPlaying = true;
        } else {
            Log.e("Spotify", "PlayerApi is null. Spotify connection might not be established.");
        }
    }


    public void resume() {
//        playerApi.play(currentPlaylist);
        playerApi.resume();
        isPlaying = true;
    }

    public void pause() {
        playerApi.pause();
        isPlaying = false;

    }

    public void next() {
        playerApi.skipNext();
        this.update();
    }

    public void previous() {
        playerApi.skipPrevious();
        this.update();
    }

    public void toggleShuffle() {
        playerApi.toggleShuffle();
    }

    public void toggleRepeat() {
        playerApi.toggleRepeat();
    }

    public void slide(int pos) {
        this.update();
        progress = pos;
        playerApi.seekTo(pos);
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void update() {
        playerApi.subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if(track != null) {

                if(!playerState.track.uri.equals(currentSongURI) && currentSongURI != null) //TODO: if repeat is ON
                    songChanged = true;
                currentSongURI = playerState.track.uri;


                nom = track.name;
                artiste = track.artist.name;
                mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(
                        data -> cover = data
                );
                lenght = track.duration;
                progress = playerState.playbackPosition;

                isPlaying = !playerState.isPaused;
            }
        });
    }



    public boolean isConnected() {
        return isConnected;
    }

    public long getLenght() {
        return lenght;
    }

    public boolean isSongChanged() {
        return songChanged;
    }

    public void resetSongChanged() {
        songChanged = false;
    }

    public long getProgress() {
        return progress;
    }

    public int getCurrentPlaylistIndex() {
        return currentPlaylistIndex;
    }
    public void setCurrentPlaylist(String currentPlaylist) {
//        this.currentPlaylist = currentPlaylist;
        playerApi.play(currentPlaylist);
        this.update();
    }

}



