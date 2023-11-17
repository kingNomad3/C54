package com.example.tp1;

public class Playliste {
    Integer imageCover;
    String nomChanson;
    String nomArtiste;
    String description;
    String nbSongs;
    String duree;
    String link;


    public Playliste(Integer imageCover, String nomChanson, String nomArtiste, String description, String nbSongs, String duree, String link) {
        this.imageCover = imageCover;
        this.nomChanson = nomChanson;
        this.nomArtiste = nomArtiste;
        this.description = description;
        this.nbSongs = nbSongs;
        this.duree = duree;
        this.link = link;
    }

}
