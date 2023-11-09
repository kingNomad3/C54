package com.eric.labonte.appsaveinstancestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class IdentificationActivity extends AppCompatActivity {

    EditText champPrenom, champNom;
    Button confirmer;
    private Context context;
    Utilisateur user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        champNom = findViewById(R.id.champNom);
        champPrenom = findViewById(R.id.champPrenom);
        confirmer =findViewById(R.id.boutonConfirmer);


        Ecouteur ec = new Ecouteur();
        confirmer.setOnClickListener(ec);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try (
                FileOutputStream fos =openFileOutput("fichier.ser", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        )
        {
            oos.writeObject(user);

        }catch (Exception o){
            o.printStackTrace();
        }


    }

    private class Ecouteur implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == confirmer){
                Intent retour = new Intent(IdentificationActivity.this, IdentificationActivity.class);
                //ceci fonctionne car nous avons serialized la class Utilisateur, plus tard on verra du Gson (json)
                retour.putExtra("user",new Utilisateur(champPrenom.getText().toString(),champNom.getText().toString()));
               //result ok veut dire qu'il n'y a pas eu de problem
                //setResultat et non start activity make sure que sa retourne dans l'acitivite et non une nouvelle activity
                setResult(RESULT_OK,retour);
                finish();
            }
        }
    }


}