package com.bioinspiredflight.physics;

import com.bioinspiredflight.ui.InputToOutput;

import javax.vecmath.Vector3d;

public class ControlMod implements ModVisitable{

    Vector3d controlMod;

    ControlMod(InputToOutput io){
        this.controlMod = io.getVector();
    }



    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }
}


