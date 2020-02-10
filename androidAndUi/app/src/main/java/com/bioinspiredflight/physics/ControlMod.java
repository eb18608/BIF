package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.ui.InputToOutput;

import javax.vecmath.Vector3d;

public class ControlMod implements ModVisitable{

    Vector3d controlMod;

    public ControlMod(InputToOutput io){
        this.controlMod = io.getVector();
    }


    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }

    @Override
    public void accept(Visitor visit, Movement movement){
        visit.visit(this, movement);
    }

    @Override
    public void accept(Visitor visit, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {

    }

}


