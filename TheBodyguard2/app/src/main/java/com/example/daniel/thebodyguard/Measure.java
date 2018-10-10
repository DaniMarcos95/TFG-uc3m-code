package com.example.daniel.thebodyguard;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by daniel on 19/04/18.
 */

public class Measure  extends AppCompatActivity implements SensorEventListener{

    private float accX, accY, accZ, gyrX, gyrY, gyrZ;
    private long current_time = 0;
    private long prev_time = 0;
    private List<Measurement> list_measures = new ArrayList<>();
    private SensorManager sm;

    public Measure(List<Measurement> list_measures){
        this.list_measures = list_measures;
    }

    public void start_sensors(){
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorAcc = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> sensorGyr = sm.getSensorList(Sensor.TYPE_GYROSCOPE);
        if (sensorAcc.size() > 0) {
            sm.registerListener(this, sensorAcc.get(0), 35000);
        }
        if (sensorGyr.size() > 0) {
            sm.registerListener(this, sensorGyr.get(0), 35000);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {

            long diff_time;

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accX = event.values[0];
                accY = event.values[1];
                accZ = event.values[2];
                current_time = System.currentTimeMillis();

            }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyrX = event.values[0];
                gyrY = event.values[1];
                gyrZ = event.values[2];
                current_time = System.currentTimeMillis();
            }else{
                current_time = System.currentTimeMillis();
            }

            if(prev_time == 0){
                prev_time = current_time;
                diff_time = 0;
            }else{
                diff_time =  current_time - prev_time;
                prev_time = current_time;
            }

            if(diff_time >= 15 && list_measures.size() != 75){
                list_measures.add(new Measurement(accX, accY, accZ, gyrX, gyrY, gyrZ));
            }

        }
    }

    public void sensors_off(){
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
