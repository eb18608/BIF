package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;

import javax.vecmath.Vector3d;

public class EnviroMod implements ModVisitable{


        Vector3d environMod;

        EnviroMod(Vector3d moveVector){
            this.environMod = moveVector;
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


