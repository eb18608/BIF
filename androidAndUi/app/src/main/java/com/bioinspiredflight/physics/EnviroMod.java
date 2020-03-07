package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;

import javax.vecmath.Vector3d;

import processing.core.PVector;

public class EnviroMod implements ModVisitable{


        PVector environMod;

        EnviroMod(PVector moveVector){
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
    public void accept(Visitor visit, Movement movement, GameSketch sketch) {

    }

    @Override
    public void accept(Visitor visit, Movement movement, GameSketch sketch, GameObject gameObject) {

    }

}


