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

    ArrayList<Integer> seekBarValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbarSonnerie = findViewById(R.id.seekBarSonnerie);
        seekBarMedia = findViewById(R.id.seekBarMedia);
        seekBarNotif = findViewById(R.id.seekBarNotif);


        seekBarValues = deSerializeList("fichier.ser");

        // Initialize SeekBars with default values if not previously serialized
        if (seekBarValues == null) {
            seekBarValues = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                seekBarValues.add(0);
            }
        }

        setupSeekBar(seekbarSonnerie, 0);
        setupSeekBar(seekBarMedia, 1);
        setupSeekBar(seekBarNotif, 2);
    }

    private void setupSeekBar(SeekBar seekBar, final int index) {
        seekBar.setProgress(seekBarValues.get(index));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValues.set(index, progress);
                serializeList(seekBarValues, "fichier.ser");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void serializeList(ArrayList<Integer> list, String filename) {
        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> deSerializeList(String filename) {
        try (FileInputStream fis = this.openFileInput(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<Integer>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}