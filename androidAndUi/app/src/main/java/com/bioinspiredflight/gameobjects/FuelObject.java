package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class FuelObject extends GameObject {
    boolean visible;
    float rotation;
    PImage icon;

    public FuelObject(GameSketch sketch, PShape body, float x, float y, float z, float scale, int id) {
        super(sketch, body, x, y, z, id);
        h = 50 * scale;
        w = 100 * scale;
        d = 100 * scale;
        visible = true;
        icon = sketch.loadImage("FuelIcon.png");
    }

    public PVector getCoords(){
        return this.coords;
    }

    public float getH(){
        return h;
    }

    public float getW() { return w; }

    public float getD()  { return d; }

    @Override
    public boolean isVisible() { return visible; }

    @Override
    public void draw3D() {
        if (sketch.getCurrentLoopID() == this.getID()) {
            visible = true;
        }
        if ( this.isVisible() ) {

            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.rotateY(rotation);
            rotation += 0.05f;
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() {
        if ((sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500) && this.isVisible() && SensorContent.ITEMS.get(3).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10, -this.getCoords().z/10);
            sketch.image(icon, 0, 0, this.getW()/10, this.getH()/10);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {

    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
