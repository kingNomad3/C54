package com.eric.labonte.appsaveinstancestate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AccueilActivity extends AppCompatActivity {
    Button startAc;

    Ecouteur ec;
    ActivityResultLauncher<Intent> lanceur;
    Utilisateur user;

    TextView salut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startAc = findViewById(R.id.boutonStartActivityForResult);
        salut = findViewById(R.id.texteSalutations);

        ec = new Ecouteur();
        startAc.setOnClickListener(ec);

        //boomrange
        lanceur = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new CallBackIdentification());


        //deseralisation
        try (
                FileInputStream fis = openFileInput("fichier.ser");
                //buffer special pour les objets
                ObjectInputStream oos = new ObjectInputStream(fis);
        ) {
            user = (Utilisateur) oos.readObject();
            salut.setText("Salut" + user.getNom() + "" + user.getPrenom());

        } catch (Exception o) {
            try {
                user = (Utilisateur) savedInstanceState.getSerializable("user");
                salut.setText("Bonjour " + user.getNom() + user.getPrenom());
            } catch (NullPointerException np) {
                np.printStackTrace();
                salut.setText("Bonjour");
            }


//      autre facon
//        try {
//            user = (Utilisateur) savedInstanceState.getSerializable("user");
//            salut.setText("Bonjour " + user.getNom() + user.getPrenom());
//        }catch (NullPointerException np){
//            np.printStackTrace();
//            salut.setText("Bonjour");
//        }

        }
//    android:configChanges="orientation|screenSize|keyboard|keyboardHidden" dans le manifest
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("user",user);
//    }

    }

    private class CallBackIdentification implements ActivityResultCallback<ActivityResult> {
        @Override
        public void onActivityResult(ActivityResult result) {
            //c'est ici que le boomrang  va revenir
            if (result.getResultCode() == RESULT_OK) {
                user = (Utilisateur) result.getData().getSerializableExtra("user");
                salut.setText("Bonjour " + user.getNom() + user.getPrenom());
            }
        }
    }

    private class Ecouteur implements AdapterView.OnClickListener {
        @Override
        public void onClick(View v) {
            lanceur.launch(new Intent(AccueilActivity.this, IdentificationActivity.class));
        }
    }
}