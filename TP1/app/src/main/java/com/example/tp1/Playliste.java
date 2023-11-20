package com.example.tp1;

public class Playliste {
    Integer imageCover;
    String nomChanson;
    String nomArtiste;
    String description;
    String nbSongs;
    String duree;
    String lien;


    public Playliste(Integer imageCover, String nomChanson, String nomArtiste, String description, String nbSongs, String duree, String lien) {
        this.imageCover = imageCover;
        this.nomChanson = nomChanson;
        this.nomArtiste = nomArtiste;
        this.description = description;
        this.nbSongs = nbSongs;
        this.duree = duree;
        this.lien = lien;
    }

}
