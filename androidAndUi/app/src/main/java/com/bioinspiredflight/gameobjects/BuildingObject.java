package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PApplet;
import processing.core.PVector;

public class BuildingObject extends GameObject {
    private float h, w, d;

    public BuildingObject(GameSketch sketch, float x, float y, float z,
                          float scale) {
        super(sketch, x, y, z);
        objectFileName = "textured_drone_sans_propellers.obj";
        loadShape();
        h = 600 * scale;
        w = 400 * scale;
        d = 400 * scale;
    }

    public BuildingObject(GameSketch sketch, float wid, float hei, float dep,
                          float x, float y, float z) {
        super(sketch, x, y, z);
        objectFileName = "textured_drone_sans_propellers.obj";
        loadShape();
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
    public void draw() {
        sketch.shape(body);
        sketch.renderBuilding(this.getW(), this.getH(), this.getD());
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
    }

    @Override
    public boolean isDrone() {
        //return false
        return false;
    }
}
