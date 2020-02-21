package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PApplet;
import processing.core.PVector;

public class BuildingObject extends GameObject {
    private float h, w, d;

    public BuildingObject(PApplet sketch, float wid, float hei, float dep,
                          float x, float y, float z, String buildingFilename) {
        super(sketch, x, y, z, buildingFilename);
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

    public void draw() {
        sketch.shape(body);
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
    }
}
