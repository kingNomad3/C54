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
    private String currentPlaylist = "spotify:playlist:1Q6ivYwu0sg0DEwHr92Jtf";
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;

    //info player
    private boolean isConnected = false;
    private String currentSongURI;
    private boolean isPlaying;
    private boolean songChanged;

    private static final String REDIRECT_URI = "com.example.tp1://callback";
    //info chanson:
    private String name;
    private String artist;
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

        //connect to App Remote
        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        playerApi = mSpotifyAppRemote.getPlayerApi();

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
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
        playerApi.play(currentPlaylist);
        isPlaying = true;
    }

    //ACTIONS
    public void resume() {
        playerApi.play(currentPlaylist);
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

    public void update() {
        playerApi.subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if(track != null) {
                //get player info:
                if(!playerState.track.uri.equals(currentSongURI) && currentSongURI != null) //TODO: if repeat is ON
                    songChanged = true;
                currentSongURI = playerState.track.uri;

                //get song info:
                name = track.name;
                artist = track.artist.name;
                mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(
                        data -> cover = data
                );
                lenght = track.duration;
                progress = playerState.playbackPosition;

                isPlaying = !playerState.isPaused;
            }
        });
    }

    //OTHERS
    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public long getLenght() {
        return lenght;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public Bitmap getCover() {
        return cover;
    }

    public boolean isSongChanged() {
        return songChanged;
    }

    public void resetSongChanged() {
        songChanged = false;
    }
    public void setCurrentPlaylistIndex(int index) {
        this.currentPlaylistIndex = index;
    }


    public long getProgress() {
        return progress;
    }

    public int getCurrentPlaylistIndex() {
        return currentPlaylistIndex;
    }
    public void setCurrentPlaylist(String currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
        playerApi.play(currentPlaylist);
        this.update();
    }
}



