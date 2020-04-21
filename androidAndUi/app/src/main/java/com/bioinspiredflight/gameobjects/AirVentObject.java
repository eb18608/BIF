package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import java.io.SyncFailedException;

import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class AirVentObject extends GameObject {
    PImage icon;
    float rotation= 0.025f;;
    public AirVentObject(GameSketch sketch, PShape body, float x, float y, float z,
                         float scale, int id) {
        super(sketch, body, x, y, z, id);
        // Same hitbox as skyscraper (not accurate at all)
        h = 60 * scale;
        w = 100 * scale;
        d = 100 * scale;
        icon = sketch.loadImage("FanIcon.png");
        this.setSolid(false);
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
        sketch.tint(211, 126);
        sketch.rotateY(rotation);
        rotation += 0.025f;
        sketch.shape(body);
        sketch.popMatrix();
    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500 && SensorContent.ITEMS.get(7).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10, -this.getCoords().z/10);
            sketch.image(icon, 0, 0, this.getW()/10, this.getD()/10);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());

    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
