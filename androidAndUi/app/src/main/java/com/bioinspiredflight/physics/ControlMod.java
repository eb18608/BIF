package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.ui.InputToOutput;

import processing.core.PVector;

public class ControlMod implements ModVisitable{

    PVector controlMod;

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
    public void accept(Visitor visit, Movement movement, GameSketch sketch) {
        visit.visit(this, movement, sketch);
    }

    @Override
    public void accept(Visitor visit, Movement movement, GameSketch sketch, GameObject gameObject) {

    }



}


