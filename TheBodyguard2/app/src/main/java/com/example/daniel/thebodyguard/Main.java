package com.example.daniel.thebodyguard;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    List<Measurement> list_measures = new ArrayList<>();
    Measure measure = new Measure(list_measures);
    Feature feature = new Feature();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        measure.sensors_off();
    }

    public void stop_monitoring(View view) {

    }

    public void start_monitoring(View view) {
        measure.start_sensors();

        while(true) {

            if(list_measures.size() == 75){
                feature.extractFeatures(list_measures);
                    //Call classifier
            }
        }
    }
}
