package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.core.PImage;

public class LoopObject extends ObjectiveObject {
    PShape movingBody;
    float movingBodyRotation;

    public LoopObject(GameSketch sketch, PShape staticBody, PShape movingBody, float x, float y, float z,
                          float scale, int id) {
        super(sketch, staticBody, x, y, z, scale, id);
        h = 140 * scale;
        w = 140 * scale;
        d = 10 * scale;
        this.movingBody = movingBody;
    }

    public PVector getCoords(){
        return this.coords;
    }

    public float getH(){
        return h;
    }

    public float getW() {
        return w;
    }

    public float getD() {
        return d;
    }

    @Override
    public void draw3D() {
        //WE NEED THIS CODE FOR FUTURE REFERENCE
        //PLEASE DON'T DELETE

        sketch.pushMatrix();
        sketch.translate(getCoords().x, getCoords().y, getCoords().z);
        sketch.shape(body);
        sketch.rotateY(movingBodyRotation);
        movingBodyRotation += 0.025f;
        sketch.shape(movingBody);
        sketch.popMatrix();

    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10 - this.getW()/20, -this.getCoords().z/10 - this.getD()/20);
            sketch.fill(252, 186, 3);
            sketch.circle(0, 0, 10);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
        System.out.println("YOU'VE HIT THIS CLASS");
        System.out.println(this.getClass());
        System.out.println("PLEASE DON'T HIT THE CLASS");
    }

    @Override
    public boolean isDrone() {
        return false;
    }
}
