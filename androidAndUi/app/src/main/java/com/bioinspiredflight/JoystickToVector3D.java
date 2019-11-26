package com.bioinspiredflight;

import javax.vecmath.Vector3d;

public class JoystickToVector3D implements Joystick.JoystickListener {

    //final private Joystick joystick;
    //final private float x, y;
    final private Vector3d vector3d;

    public JoystickToVector3D(){
        vector3d = new Vector3d();
        //this.joystick = joystick;
        //this.x = 0;
        //this.y = 0;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        //System.out.printf("Joystick X: %.3f, Y: %.3f\n", xPercent, yPercent);
        vector3d.setX(xPercent);
        vector3d.setY(-yPercent);
        vector3d.setZ(0);
        System.out.println(vector3d.toString());
    }

    public Vector3d getVector(){
        return vector3d;
    }

}
