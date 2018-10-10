package com.example.daniel.thebodyguard;

/**
 * Created by daniel on 19/04/18.
 */

public class Measurement {

    private float accX, accY, accZ, gyrX, gyrY, gyrZ;

    public Measurement(float accX, float accY, float accZ, float gyrX, float gyrY, float gyrZ){
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyrX = gyrX;
        this.gyrY = gyrY;
        this.gyrZ = gyrZ;
    }

    public float getAccX() {
        return this.accX;
    }

    public void setAccX(float accX) {
        this.accX = accX;
    }

    public float getAccY() {
        return this.accY;
    }

    public void setAccY(float accY) {
        this.accY = accY;
    }

    public float getAccZ() {
        return this.accZ;
    }

    public void setAccZ(float accZ) {
        this.accZ = accZ;
    }

    public float getGyrX() {
        return this.gyrX;
    }

    public void setGyrX(float gyrX) {
        this.gyrX = gyrX;
    }

    public float getGyrY() {
        return this.gyrY;
    }

    public void setGyrY(float gyrY) {
        this.gyrY = gyrY;
    }

    public float getGyrZ() {
        return this.gyrZ;
    }

    public void setGyrZ(float gyrZ) {
        this.gyrZ = gyrZ;
    }
}
