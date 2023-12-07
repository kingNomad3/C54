package com.bousquet.noe.examen_2_b;


import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class Stock {
    public static Stock instance;
    private Vector<VehiculeHyundai> inventaire;


    public static Stock getInstance()
    {
        if ( instance == null )
            instance = new Stock();
        return instance;
    }


    private Stock()
    {
        resetInventaire();
        inventaire = getInventaire();
    }


   public void resetInventaire()
   {
       inventaire = new Vector();


       VehiculeHyundai[] tab = {new Tucson("Tucson", "essence", 3), new Tucson("Tucson", "hybride", 5), new SantaFe("SantaFe", "essence", 9),
               new SantaFe("SantaFe", "hybride rechargeable", 1),new Kona("Kona", "essence", 1), new Ioniq5("Ioniq5", 1) } ;
       inventaire.addAll(Arrays.asList(tab)); // transforme le tableau en list, peut passer List à addAll

   }

    public Vector<VehiculeHyundai> getInventaire() {
        return inventaire;
    }

    public VehiculeHyundai trouverObjet ( String nomModele, String alimentation)
    {

        for (VehiculeHyundai h : inventaire)
        {
            if ( h.getNom().equals(nomModele)  && h.getAlimentation().equals(alimentation))
                return h;
        }
        return null;
    }




}
