package com.eric.accompagnementatelier2;

public class Oeuvre {

    private String titre;
    private String nomArtiste;
    private int annee;

    public Oeuvre(String titre, String nomArtiste, int annee) {
        this.titre = titre;
        this.nomArtiste = nomArtiste;
        this.annee = annee;
    }

    public String getTitre() {
        return titre;
    }

    public String getNomArtiste() {
        return nomArtiste;
    }

    public int getAnnee() {
        return annee;
    }
}
