package com.example.annex1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class activity_ajouter extends AppCompatActivity {
    Button ajouter;
    EditText textAjouter;
    Ecouteur ec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        ajouter = findViewById(R.id.ajouterPage);

        ec = new Ecouteur();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            String temps = textAjouter.getText().toString();


            if (textAjouter.getText().toString().isEmpty()) {
                Toast.makeText(activity_ajouter.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Note note = new Note(temps);

                } catch (Exception e) {
                    Toast.makeText(activity_ajouter.this, "Error adding evaluation!", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        }
    }
}