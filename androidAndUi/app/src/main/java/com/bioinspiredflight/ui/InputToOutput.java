package com.bioinspiredflight.ui;

import com.bioinspiredflight.ui.Joystick;
import com.bioinspiredflight.ui.Slider;

import javax.vecmath.Vector3d;

public class InputToOutput implements Joystick.JoystickListener, Slider.SliderListener {

    //final private Joystick joystick;
    //final private float x, y;
    final private Vector3d vector3d;
    private float rotation;

    public InputToOutput(){
        vector3d = new Vector3d();
        vector3d.setX(0);
        vector3d.setY(0);
        vector3d.setZ(0);
        rotation = 0f;
        //this.joystick = joystick;
        //this.x = 0;
        //this.y = 0;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        //System.out.printf("Joystick X: %.3f, Y: %.3f\n", xPercent, yPercent);
        vector3d.setX(xPercent);
        vector3d.setY(-yPercent);
        //vector3d.setZ(0);
        System.out.println(vector3d.toString());
    }

    @Override
    public void onSliderMoved(float zPercent, float rotation, int id) {
        //this.zValue = zValue;
        vector3d.setZ(zPercent);
        this.rotation = rotation;
        //System.out.println(this.zValue);
        System.out.println(vector3d.toString());
    }

    public Vector3d getVector(){
        return vector3d;
    }

    public float getRotation() {
        return rotation;
    }
    /*public float getZValue(){
        return zValue;
    }*/

}
