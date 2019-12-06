package com.bioinspiredflight.physics;

import javax.vecmath.Vector3d;

public class ControlMod implements ModVisitable{

    Vector3d controlMod;

    ControlMod(Vector3d vectorXY, Vector3d vectorZ){
        this.controlMod = vectorXY;
        this.controlMod.z = vectorZ.z;
    }


    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }
}
