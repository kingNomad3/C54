package com.example.tp1;

public class Playliste {
    String nom;
    Integer duree;
    Integer nbChansons;
    Integer image;
    String lienSpotify;

    public Playliste(String nom, Integer duree, Integer nbChansons, Integer image, String lienSpotify) {
        this.nom = nom;
        this.duree = duree;
        this.nbChansons = nbChansons;
        this.image = image;
        this.lienSpotify = lienSpotify;
    }
}
