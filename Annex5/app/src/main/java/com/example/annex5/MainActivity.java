package com.example.annex5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    TextView palmares;
    TextView date;
    TextView nomChanson;
    ImageView image;

    ListView liste;

    String [] params = {"palpares","nomChanson","date","image"};
    int [] id = {R.id.palmares,R.id.date,R.id.nomChanson,R.id.image};

    Vector<Hashtable<String,Object>> Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       
        liste =  findViewById(R.id.liste);

        Map = remplir();

        SimpleAdapter Sadapt = new SimpleAdapter(this,Map,R.layout.liste_textview,params,id);
        liste.setAdapter(Sadapt);

    }

    public Vector<Hashtable<String,Object>> remplir(){

        Vector<Hashtable<String,Object>> v = new Vector();

        Hashtable<String,Object>  h = new Hashtable();
        h.put("palpares",3);
        h.put("nomChanson","touch me");
        h.put("date","22/03/86");
        h.put("image", R.drawable.touchme);
        v.add(h);

        h = new Hashtable();
        h.put("palpares",8);
        h.put("nomChanson","nothing is gonna stop me now  ");
        h.put("image", R.drawable.nothing);
        v.add(h);

        h = new Hashtable();
        h.put("palpares",31);
        h.put("nomChanson","Santa Maria");
        h.put("date","28/03/1998");
        h.put("image", R.drawable.santamaria);
        v.add(h);

        h = new Hashtable();
        h.put("palpares",108);
        h.put("nomChanson","Hot boy");
        h.put("date","10/04/2018");
        h.put("image", R.drawable.hotboy);
        v.add(h);

        return v;
    }





}