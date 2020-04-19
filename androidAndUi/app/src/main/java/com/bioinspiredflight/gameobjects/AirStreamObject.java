package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class AirStreamObject extends GameObject {
    float rot;

    public AirStreamObject(GameSketch sketch, PShape body, float x, float y, float z,
                           float scale, float rot, int id) {
        super(sketch, body, x, y, z, id);
        h = 600 * scale;
        w = 120 * scale;
        d = 120 * scale;
        this.rot = rot;
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
        if (SensorContent.ITEMS.get(0).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        //TODO: Custom airstream collision.
        // ints for each direction
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
