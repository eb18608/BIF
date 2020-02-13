package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;

import javax.vecmath.Vector3d;

import processing.core.PVector;

public class CollideMod implements ModVisitable {

    PVector collideMod;

    public CollideMod(PVector moveVector){
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
    public void accept(Visitor visit, Movement movement, GameSketch sketch) {
        visit.visit(this, movement, sketch);
    }

}
