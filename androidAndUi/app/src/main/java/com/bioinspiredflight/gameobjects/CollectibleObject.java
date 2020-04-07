package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PShape;
import processing.core.PVector;

public class CollectibleObject extends ObjectiveObject {
    boolean visible;

    public CollectibleObject(GameSketch sketch, PShape body, float x, float y, float z,
                      float scale, int id) {
        super(sketch, body, x, y, z, scale, id);
        h = 20 * scale;
        w = 20 * scale;
        d = 20 * scale;
        visible = true;
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

    public boolean isVisible() { return visible; }

    @Override
    public void draw3D() {
        if ( this.isVisible() ) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            this.body.setFill(sketch.color(0, 200, 200, 100));
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() {
        if ((sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500) && this.isVisible()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10, -this.getCoords().z/10);
            sketch.fill(sketch.color(0, 200, 200, 100));
            sketch.circle(0, 0, this.getW()/5);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        //System.out.println("we're touching, ew...");
        if (this.isVisible()) {
            //System.out.println("YOU got here");
            //System.out.println(this.getClass());
            setStatus(true);
            //System.out.println("PLEASE DON'T HIT THE CLASS");
        }
    }

    @Override
    public boolean isDrone() {
        return false;
    }
}
