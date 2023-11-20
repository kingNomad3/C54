package com.example.annex6;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    Button button;

    ActivityResultLauncher<Intent> lanceur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        lanceur = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),new CallBackImage());

        button.setOnClickListener(source ->{
            //pick : choisir une image
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            lanceur.launch(i);
        });

    }

    private class CallBackImage implements ActivityResultCallback<ActivityResult>{

        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Uri uri = result.getData().getData();
                image.setImageURI(uri);
            }





        }
    }


}