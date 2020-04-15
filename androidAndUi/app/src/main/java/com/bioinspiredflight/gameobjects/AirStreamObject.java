package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class AirStreamObject extends GameObject {

    public AirStreamObject(GameSketch sketch, PShape body, float x, float y, float z,
                           float scale, int id) {
        super(sketch, body, x, y, z, id);
        h = 600 * scale;
        w = 120 * scale;
        d = 120 * scale;
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
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.shape(body);
            sketch.popMatrix();
    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        //TODO: Custom airstream collision.
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
