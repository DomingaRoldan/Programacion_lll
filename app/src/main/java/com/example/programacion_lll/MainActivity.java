package com.example.programacion_lll;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost pestana = (TabHost)findViewById(R.id.parcial);
        pestana.setup();

        //pestana 1
        TabHost.TabSpec spec = pestana.newTabSpec ("Pestana Uno");
        spec.setContent(R.id.Universal);
        spec.setIndicator("Pestana Uno");
        pestana.addTab (spec);

        //pestana 2
        spec = pestana.newTabSpec("Pestana Dos");

        spec.setContent(R.id.Area);
        spec.setIndicator("Pestana Dos");
        pestana.addTab (spec);


    }
}
