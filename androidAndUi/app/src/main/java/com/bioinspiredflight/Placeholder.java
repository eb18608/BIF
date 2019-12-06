package com.bioinspiredflight;

import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitable;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;

import processing.core.PApplet;

/**
 * This is a placeholder class for Processing code.
 * The game is going to run from here.
 */
public class Placeholder extends PApplet{

    private Movement movingObject;
    private ControlMod controlMod;
    private InputToOutput io;
    private ModVisitor visitor;

    public void setMovingObject(Movement movingObject, ControlMod controlMod, InputToOutput io){
        this.movingObject = movingObject;
        this.controlMod = controlMod;
        this.io = io;
        this.visitor = new ModVisitor();
    }

    public void settings() {
        //size(800, 700);
        fullScreen();
    }

    public void setup() {
        ellipse(400, 400, 50, 50);

    }

    public void draw() {
        if (mousePressed) {
            ellipse(mouseX, mouseY, 50, 50);
        }
        //movingObject.visit(controlMod);
        //controlMod.update(io);
        controlMod.accept(visitor, movingObject);
    }

}
