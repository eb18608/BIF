package com.bioinspiredflight.ui;

import com.bioinspiredflight.ui.Joystick;
import com.bioinspiredflight.ui.Slider;

import javax.vecmath.Vector3d;
import com.bioinspiredflight.physics.Movement;
import processing.core.PApplet;
import processing.core.PVector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class InputToOutput implements Joystick.JoystickListener, Slider.SliderListener {

    //final private Joystick joystick;
    //final private float x, y;
    final private PVector vector3d;
    private float rotation;
    private float totalRotation;

    //Get individual component from PVector
    public float getX(PVector p){
        return p.x;
    }
    public float getY(PVector p){
        return p.y;
    }
    public float getZ(PVector p){
        return p.z;
    }

    //Set individual componend in PVector
    public void setX(PVector p, float x){
        p.x = x;
    }
    public void setY(PVector p, float y){
        p.y = y;
    }
    public void setZ(PVector p, float z){
        p.z = z;
    }
    public void setV(PVector p, float x, float y, float z){
        p.x = x;
        p.y = y;
        p.z = z;
    }

    public InputToOutput(){
        vector3d = new PVector(0.0f, 0.0f, 0.0f);
        setX(vector3d, 0);
        setY(vector3d, 0);
        setZ(vector3d, 0);
        rotation = 0f;
        totalRotation = 0f;
        //this.joystick = joystick;
        //this.x = 0;
        //this.y = 0;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        //System.out.printf("Joystick X: %.3f, Y: %.3f\n", xPercent, yPercent);
        setX(vector3d, xPercent);
        setY(vector3d, -yPercent);
        rotateVector();
        //vector3d.setZ(0);
        //System.out.println(vector3d.toString());
    }

    @Override
    public void onSliderMoved(float zPercent, float rotation, int id) {
        //this.zValue = zValue;
        setZ(vector3d, zPercent);
        this.rotation = rotation;
        //this.totalRotation -= rotation * rotationSpeed;
        //System.out.println(this.zValue);
        //System.out.println(vector3d.toString());
    }

    public PVector getVector(){
        return vector3d;
    }

    public float getRotation() {
        return rotation;
    }

    public float getTotalRotation() {
        return totalRotation;
    }

    public void setTotalRotation(float totalRotation) {
        this.totalRotation = totalRotation;
    }

    private void rotateVector(){
        float x = getX(vector3d);
        float y = getY(vector3d);
        float newX = (float) ((cos(totalRotation) * x) - (sin(totalRotation) * y));
        float newY = (float) ((sin(totalRotation) * x) + (cos(totalRotation) * y));
        //System.out.printf("NewX: %.3f, NewY: %.3f\n", newX, newY);
        setX(vector3d, newX);
        setY(vector3d, newY);
    }

}
