package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class BuildingObject extends GameObject {
    //private float h, w, d;

    public BuildingObject(GameSketch sketch, PShape body, float x, float y, float z,
                          float scale) {
        super(sketch, body, x, y, z);
        //objectFileName = "textured_drone_sans_propellers.obj";
        //loadShape();
        h = 600 * scale;
        w = 400 * scale;
        d = 400 * scale;
    }

    public BuildingObject(GameSketch sketch, PShape body, float wid, float hei, float dep,
                          float x, float y, float z) {
        super(sketch, body, x, y, z);
        //objectFileName = "textured_drone_sans_propellers.obj";
        //loadShape();
        h = hei;
        w = wid;
        d = dep;
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
        sketch.translate(getCoords().x,
                getCoords().y, getCoords().z);
        sketch.shape(body);
        //sketch.renderBuilding(this.getW(), this.getH(), this.getD());
        sketch.popMatrix();



        sketch.pushMatrix();
        sketch.translate(getCoords().x - getW()/2,
                getCoords().y - getH()/2, getCoords().z - getD()/2);
        sketch.renderBuilding(this.getW(), this.getH(), this.getD());
        sketch.popMatrix();
    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10 - this.getW()/20, -this.getCoords().z/10 - this.getD()/20);
            sketch.fill(200);
            sketch.rect(0, 0, this.getW()/10, this.getD()/10);
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
        //return false
        return false;
    }
}
