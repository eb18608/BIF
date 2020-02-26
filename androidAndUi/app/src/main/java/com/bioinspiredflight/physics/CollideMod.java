package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;

import javax.vecmath.Vector3d;

import processing.core.PVector;

public class CollideMod implements ModVisitable {

    public PVector collideMod;

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

    @Override
    public void accept(Visitor visit, Movement movement, GameSketch sketch, GameObject gameObject) {
        visit.visit(this, movement, sketch, gameObject);
    }

}
