package com.bousquet.noe.examen_2_b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private static Stock stock;
    SimpleAdapter simpleAdapter;
    Vector<HashMap<String, Object>> hmStock;
    Vector<VehiculeHyundai> inventaire;
    VehiculeHyundai pickedCar;
    Commande commande;

    ListView list;
    TextView panier;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.listView);
        panier = findViewById(R.id.achatListeTexte);
        button = findViewById(R.id.commanderButton);

        stock = Stock.getInstance();
        stock.resetInventaire();
        inventaire = stock.getInventaire();
        hmStock = new Vector<>();

        for(VehiculeHyundai car:inventaire) {
            hmStock.add(
                    new HashMap(){{
                        put("nom", car.getNom());
                        put("alimentation", car.getAlimentation());
                        put("prix", car.getPrix());
                    }}
            );
        }

        simpleAdapter = new SimpleAdapter(
                this,
                hmStock,
                R.layout.layout_list,
                new String[]{"nom", "alimentation", "prix"},
                new int[]{R.id.carTitle, R.id.carType, R.id.price}
        );

        commande = new Commande(null);

        //SERIALIZE
        try {
            ObjectInputStream ois = null;
            FileInputStream fos = openFileInput("fichier.ser");
            ois = new ObjectInputStream(fos);
            commande.setVoitures((Vector<VehiculeHyundai>) ois.readObject());
            commande.setQteVoitures((int) ois.readObject());
            setPanier();
        } catch (NullPointerException np) {
            commande.setVoitures(new Vector<>());
            commande.setQteVoitures(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener((adapterView, view, pos, l) -> {

            pickedCar = stock.trouverObjet(
                    (String) hmStock.get(pos).get("nom"),
                    (String) hmStock.get(pos).get("alimentation"));

            if((Integer) pickedCar.getQte() > 0 && commande.getQteVoitures() <= 2) {
                commande.ajouterVoiture(pickedCar, pickedCar.getQte());
                panier.append(pickedCar.getNom() + " - " + pickedCar.getAlimentation() + '\n');
           }
        });

        button.setOnClickListener(v -> {
            panier.setText(commande.getPrixTotal().toString());
            resetApp();
        });
    }

    public void setPanier() {
        for (VehiculeHyundai car : commande.getVoitures()) {
            panier.append(car.getNom() + " - " + car.getAlimentation() + '\n');
            //resetApp();
        }
    }

    public void resetApp() {
        panier.setText("");
        stock.resetInventaire();

        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = this.openFileOutput("fichier.ser", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.flush();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        ObjectOutputStream oos = null;

        try {
            FileOutputStream fos = this.openFileOutput("fichier.ser", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(commande.getVoitures());
            oos.writeObject(commande.getQteVoitures());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}