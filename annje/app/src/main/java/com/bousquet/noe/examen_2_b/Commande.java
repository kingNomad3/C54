package com.bousquet.noe.examen_2_b;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class Commande implements Serializable {

    Integer qteVoitures;
    Double prixTotal;
    Vector<VehiculeHyundai> voitures;

    public Commande(Vector<VehiculeHyundai> voitures) {
        this.qteVoitures = 0;
        this.prixTotal = 0.0;
        this.voitures = voitures;
        if(voitures == null)
            this.voitures = new Vector<>();
    }

    public void ajouterVoiture(VehiculeHyundai voiture, Integer qte) {
        voitures.add(voiture);
        qteVoitures += 1;
        voiture.setQte(qte - 1);
    }

    public Double getPrixTotal() {
        prixTotal = 0.0;
        for (VehiculeHyundai car:voitures) {
            prixTotal += car.getPrix();
        }
        return prixTotal;
    }

    public Integer getQteVoitures() {
        return qteVoitures;
    }

    public Vector<VehiculeHyundai> getVoitures() {
        return voitures;
    }

    public void setVoitures(Vector<VehiculeHyundai> voitures) {
        this.voitures = voitures;
    }

    public void setQteVoitures(Integer qteVoitures) {
        this.qteVoitures = qteVoitures;
    }
}
