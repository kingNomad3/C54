package com.eric.appannexe7;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button acheter,appeler,ouEst,photo,message;
    ImageView image;
    ActivityResultLauncher<Intent> lanceur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acheter = findViewById(R.id.boutonLivre);
        appeler = findViewById(R.id.boutonAppel);
        ouEst = findViewById(R.id.boutonVille);
        photo = findViewById(R.id.boutonPhoto);
        message = findViewById(R.id.boutonMessage);
        image = findViewById(R.id.imageView);

        lanceur = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            Bundle extras = result.getData().getExtras(); // les extras fournis dans le intente de retour
            Bitmap bit = (Bitmap) extras.get("data"); // un extras particulier ayant la cle data qui contient le bitmap
            image.setImageBitmap(bit);  // j affiche le bitmap dans le image view
        });

        acheter.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse( "https://www.leslibraires.ca/"));
            startActivity(i);
        });

        appeler.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse( "tel:+789789"));
            startActivity(i);
        });

        ouEst.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q=ville, +province, +pays "));
            startActivity(i);
        });

        photo.setOnClickListener(source -> {
            Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            lanceur.launch(takePicIntent);
        });

        message.setOnClickListener(source -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT,"allo");
            i.setType("text/plain");


            Intent ShareIntent =  Intent.createChooser(i,null);
            startActivity(ShareIntent);
        });
    }

    private class CallBackImage implements ActivityResultCallback<ActivityResult> {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                Uri uri = result.getData().getData();
                image.setImageURI(uri);
            }
        }
    }
}