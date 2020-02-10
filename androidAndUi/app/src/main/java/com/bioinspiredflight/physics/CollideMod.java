package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;

import javax.vecmath.Vector3d;

public class CollideMod implements ModVisitable {

    Vector3d collideMod;

    public CollideMod(Vector3d moveVector){
        this.collideMod = moveVector;
    }


    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }

    @Override
    public void accept(Visitor visit, Movement movement) {

    }


    @Override
    public void accept(Visitor visit, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {
        visit.visit(this, movement, buildingObject, drone);
    }
}
