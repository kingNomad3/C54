package com.example.annex1;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.crypto.Cipher;

public class SingletonMemo  {
    public static SingletonMemo instance;
    private  ArrayList<String> liste;

    private Context context;

    private SingletonMemo(Context context) {
        liste = new ArrayList<>();
        this.context = context;
    }

    public static SingletonMemo getInstance(Context contexte) {
        if (instance == null)
            //si l'instance est null alors je creer mon instence unique
            instance = new SingletonMemo(contexte);
        return instance;
    }

    public ArrayList<String> getListe() {
        return liste;
    }

    public void AjouterMemo(String memo) {
         liste.add(memo);
    }

    public void setListe(ArrayList<String> liste) {
        this.liste = liste;
    }

    //pas besoin de extends seriazable dans la classe car le array liste est deja seriazable
    public void seriazableListe(){ // throws Exception
        //On n;a pas acces a open file output car nous sommes dans une classe normal et non une activite alors on utilise le context
        //ici on va utiliser mode private car mode append va garder l'ancienne liste ici on veut ecraser la vielle et mettre la nouvelle (avec les anciens memo)
        try (
            FileOutputStream fos = context.openFileOutput("fichier.ser", Context.MODE_PRIVATE);
            //buffer special pour les objets
            ObjectOutputStream oos = new ObjectOutputStream(fos)
            )
        {
            oos.writeObject(liste);

        }catch (Exception o){
            o.printStackTrace();
        }
    }

    public ArrayList<String> deSeriazableListe() {
        try (
            FileInputStream fis = context.openFileInput("fichier.ser");
            //buffer special pour les objets
            ObjectInputStream ois = new ObjectInputStream(fis))
        {
            liste = (ArrayList<String>) ois.readObject();

        }catch (Exception o){
            o.printStackTrace();
        }
        return liste;
    }
}
