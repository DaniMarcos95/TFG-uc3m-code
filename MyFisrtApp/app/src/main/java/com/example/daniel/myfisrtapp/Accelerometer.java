package com.example.daniel.myfisrtapp;

import android.content.SyncStatusObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class Accelerometer extends AppCompatActivity implements SensorEventListener{

    private float curX = 0, curY = 0, curZ = 0;
    private SensorManager sm;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;
    private File f;
    private OutputStreamWriter fout;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        comprobarEstado();

        if(sdAccesoEscritura == true){
            crearFichero();
        }
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();

        if(wakeLock.isHeld() == true){
            ((TextView) findViewById(R.id.txtSDstate)).setText("WakeLock on");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), 150000/*SensorManager.SENSOR_DELAY_NORMAL*/);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        //sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 150000);
    }

     @Override
    protected void onPause() {
        super.onPause();
        //sm.unregisterListener(this);
         //wakeLock.release();
    }

    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
        try{
            fout.close();
        }catch (Exception ex) {
            Log.e("Ficheros", "Error al cerrar fichero en tarjeta SD");
        }
        wakeLock.release();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) { /*Evitar problemas de concurrencia*/
            long current_time = event.timestamp;
            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            ((TextView) findViewById(R.id.txtAccX)).setText("Acelerómetro X: " + curX);
            ((TextView) findViewById(R.id.txtAccY)).setText("Acelerómetro Y: " + curY);
            ((TextView) findViewById(R.id.txtAccZ)).setText("Acelerómetro Z: " + curZ);
            guardarDatos(curX, curY, curZ);
        }
    }

    public void comprobarEstado(){
        String estado = Environment.getExternalStorageState();

        if(estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible = true;
            sdAccesoEscritura = true;
        }else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            sdDisponible = true;
            sdAccesoEscritura = false;
        }else{
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
        //((TextView) findViewById(R.id.txtSDstate)).setText("Estado de la tarjeta SD: " + estado);
    }

    public void crearFichero(){
        try{
            File ruta_sd = getExternalFilesDir(null);
            f = new File(ruta_sd, "datosacelerometro.txt");
            fout = new OutputStreamWriter(new FileOutputStream(f));
            Toast.makeText(this,ruta_sd.getAbsolutePath(),Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al crear fichero en tarjeta SD");
        }
    }

    public void guardarDatos(float curX, float curY, float curZ){
        try {
            fout.write("X:" + curX + " Y:" + curY + " Z:" + curZ + "\n");
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }
    }
}
