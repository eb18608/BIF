package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PShape;
import processing.core.PVector;

public class HitboxObject extends GameObject {
    public PVector coords;  //keeping this public for optimization reasons
    GameSketch sketch;
    float h, w, d;
    float roty;
    int id;

    public HitboxObject(GameSketch sketch, PShape body, float x, float y, float z, int id){
        super(sketch, body, x, y, z, id);
        this.sketch = sketch;
        this.coords = new PVector(x, y, z);
        this.id = id;
        this.roty = roty;
    }

    public void setHWD(float hei, float wid, float dep) {
        this.h = hei;
        this.w = wid;
        this.d = dep;
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
    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
    }

    @Override
    public boolean isDrone() { return false; }

    @Override
    public boolean shouldBeTracked() { return false; }

    @Override
    public boolean isVisible() { return false; }

}
