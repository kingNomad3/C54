package com.bousquet.noe.examen_2_b;

public class Kona extends VehiculeHyundai{
    public Kona ( String nom, String alimentation, int qte)
    {
        super ( nom, alimentation, qte);
        if ( alimentation.equals("essence"))
            setPrix(25000);
        else if ( alimentation.equals("hybride"))
            setPrix(42954);
        else
            setPrix(47050);  // hybride rechargeable
    }
}
