package com.bioinspiredflight;

public class JoystickToVector3D implements Joystick.JoystickListener {

    //final private Joystick joystick;
    //final private float x, y;
    //final private Vector3d

    public JoystickToVector3D(){
        //this.joystick = joystick;
        //this.x = 0;
        //this.y = 0;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        System.out.printf("Joystick X: %.3f, Y: %.3f\n", xPercent, yPercent);
    }

}
