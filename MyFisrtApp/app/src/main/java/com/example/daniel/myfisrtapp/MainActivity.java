package com.example.daniel.myfisrtapp;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float accX = 0, accY = 0, accZ = 0, gyrX = 0, gyrY = 0, gyrZ = 0;
    private SensorManager sm;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;
    private File f;
    private OutputStreamWriter fout;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    EditText et1;
    private String nombre_archivo;
    private long current_time = 0;
    private long prev_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText)findViewById(R.id.introducir_nombre);
        ((TextView) findViewById(R.id.nombre_archivo)).setText("Introduzca el nombre de archivo:");
        comprobarEstado();

        PowerManager pm = (PowerManager)   getSystemService(this.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");
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
        wakeLock.acquire();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wakeLock.acquire();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        try{
            fout.close();
        }catch (Exception ex) {
            Log.e("Ficheros", "Error al cerrar fichero en tarjeta SD");
            wakeLock.release();
        }
    }

    //Method to start capturing data when the 'Start' button is pressed
    public void StartMeasure(View view) {
        // Do something in response to button
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

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    //Method to save data from the sensors whenever a change is detected
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

            ((TextView) findViewById(R.id.txtAccX)).setText("Eje X: " + accX + " " + gyrX);
            ((TextView) findViewById(R.id.txtAccY)).setText("Eje Y: " + accY + " " + gyrY);
            ((TextView) findViewById(R.id.txtAccZ)).setText("Eje Z: " + accZ + " " + gyrZ);

            if(diff_time >= 15) {
                guardarDatos(accX, accY, accZ, gyrX, gyrY, gyrZ, diff_time);
            }
            System.out.println(System.currentTimeMillis());
        }
    }

    //Method to check if output file is ready to be written
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
    }

    //Method to create a new file
    public void crearFichero(View view){
        try{
            nombre_archivo = et1.getText().toString() + ".txt";
            File ruta_sd = getExternalFilesDir(null);
            f = new File(ruta_sd, nombre_archivo);
            fout = new OutputStreamWriter(new FileOutputStream(f));
            Toast.makeText(this,ruta_sd.getAbsolutePath(),Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al crear fichero en tarjeta SD");
        }
    }

    //Method to save data in the corresponding file
    public void guardarDatos(float curX, float curY, float curZ, float gyrX, float gyrY, float gyrZ, long diff_time){
        try {
            fout.write(" " + curX + " " + curY + " " + curZ + " " + gyrX + " " + gyrY + " " + gyrZ + " " + diff_time + "\n");
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }
    }

    public void reiniciarActivity(View view){
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        try{
            fout.close();
        }catch (Exception ex) {
            Log.e("Ficheros", "Error al cerrar fichero en tarjeta SD");
            wakeLock.release();
        }
    }
}

