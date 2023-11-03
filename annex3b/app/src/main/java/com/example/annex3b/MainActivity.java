package com.example.annex3b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SeekBar seekbarSonnerie;
    SeekBar seekBarMedia;
    SeekBar seekBarNotif;

    ArrayList<Integer> seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbarSonnerie = findViewById(R.id.seekBarSonnerie);
        seekBarMedia = findViewById(R.id.seekBarMedia);
        seekBarNotif = findViewById(R.id.seekBarNotif);



        seekBar.add(seekbarSonnerie.getProgress());
        seekBar.add(seekBarMedia.getProgress());
        seekBar.add(seekBarNotif.getProgress());






    }

    public void seriazableListeer(){
        try (
                FileOutputStream fos = this.openFileOutput("seekbar.ser", Context.MODE_PRIVATE);
                //buffer special pour les objets
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        )
        {
            oos.writeObject(seekBar);

        }catch (Exception o){
            o.printStackTrace();
        }
    }

    public ArrayList<Integer> deSeriazableListe() {
        try (
                FileInputStream fis = this.openFileInput("seekbar.ser");
                //buffer special pour les objets
                ObjectInputStream ois = new ObjectInputStream(fis))
        {
            seekBar = (ArrayList<Integer>) ois.readObject();

        }catch (Exception o){
            o.printStackTrace();
        }
        return seekBar;
    }



    @Override
    protected void onStart() {
        super.onStart();




    }

    protected void onStop() {
        super.onStop();

    }

}