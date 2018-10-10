package com.example.daniel.myfisrtapp;

import android.os.Environment;
import java.lang.String;

/**
 * Created by Daniel on 21/09/2017.
 */

public class AlmacenDatos extends Environment{

    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;

    //Comprobamos el estado de la memoria externa (tarjeta SD)
    String estado = Environment.getExternalStorageState();


/*
    if (!Environment.MEDIA_MOUNTED.compareTo(estado)){
            sdDisponible = true;
            sdAccesoEscritura = true;
        }else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            sdDisponible = true;
            sdAccesoEscritura = false;
        }else{
            sdDisponible = false;
            sdAccesoEscritura = false;
        }*/
}
