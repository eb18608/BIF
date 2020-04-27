package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class CollectibleObject extends ObjectiveObject {
    boolean visible;
    float rotation;

    public CollectibleObject(GameSketch sketch, PShape body, float x, float y, float z, float scale, float roty, int id) {
        super(sketch, body, x, y, z, scale, id);
        h = 6 * scale;
        w = 50 * scale;
        d = 25 * scale;
        visible = true;
        this.setSolid(false);
        rotation = roty;
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
    public boolean isVisible() { return visible; }

    @Override
    public void draw3D() {
        if ( this.isVisible() ) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.rotateY(rotation);
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() {
        if ((sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 3000) && this.isVisible() && SensorContent.ITEMS.get(4).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/20, -this.getCoords().z/20);
            sketch.fill(sketch.color(0, 200, 200, 100));
            sketch.circle(0, 0, this.getW()/5);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        if (!(!SensorContent.ITEMS.get(6).isEquipped() && sketch.getCollectiblesHeld() == 1)) {
            if (this.isVisible()) {
                setStatus(true);
                sketch.setCollectiblesHeld(sketch.getCollectiblesHeld() + 1);
            }
            visible = false;

        }
    }

    @Override
    public boolean isDrone() { return false; }

    @Override
    public boolean shouldBeTracked() {
        if (!SensorContent.ITEMS.get(6).isEquipped() && sketch.getCollectiblesHeld() == 1) {
            return false;
        } else {
            return isVisible();
        }
    }
}
