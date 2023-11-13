package com.example.annex3b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    SeekBar seekbarSonnerie;
    SeekBar seekBarMedia;
    SeekBar seekBarNotif;
//    ArrayList<Integer> seekBarValues;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbarSonnerie = findViewById(R.id.seekBarSonnerie);
        seekBarMedia = findViewById(R.id.seekBarMedia);
        seekBarNotif = findViewById(R.id.seekBarNotif);
        System.out.println("onCreate");

//        seekBarValues = deSerializeList("fichier.ser");
//
//        // Initialize SeekBars with default values if not previously serialized
//        if (seekBarValues == null) {
//            seekBarValues = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                seekBarValues.add(0);
//            }
//        }
//
//        setupSeekBar(seekbarSonnerie, 0);
//        setupSeekBar(seekBarMedia, 1);
//        setupSeekBar(seekBarNotif, 2);

        //deserialisation
        try{
            ObjectInputStream ios= null;
            FileInputStream fos = openFileInput("fichier.ser");

            ios = new ObjectInputStream(fos);

            seekBarMedia.setProgress((int)ios.readObject());
            seekBarNotif.setProgress((int)ios.readObject());
            seekbarSonnerie.setProgress((int)ios.readObject());


        }catch (ClassNotFoundException | IOException fnfe){
            fnfe.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
        //serialisation
        try(
            FileOutputStream fos = openFileOutput("fichier.ser",Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){

            oos.writeObject(seekBarMedia.getProgress());
            oos.writeObject(seekBarNotif.getProgress());
            oos.writeObject(seekbarSonnerie.getProgress());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    private void setupSeekBar(SeekBar seekBar, final int index) {
//        seekBar.setProgress(seekBarValues.get(index));
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seekBarValues.set(index, progress);
//                serializeList(seekBarValues, "fichier.ser");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//    }
//
//    private void serializeList(ArrayList<Integer> list, String filename) {
//        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ArrayList<Integer> deSerializeList(String filename) {
//        try (FileInputStream fis = this.openFileInput(filename);
//             ObjectInputStream ois = new ObjectInputStream(fis)) {
//            return (ArrayList<Integer>) ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}