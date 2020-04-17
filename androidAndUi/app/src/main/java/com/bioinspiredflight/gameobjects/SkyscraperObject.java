package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PShape;
import processing.core.PVector;

public class SkyscraperObject extends GameObject {
    float rotation;
    HitboxObject hb1, hb2, hb3, hb4;

    public SkyscraperObject(GameSketch sketch, PShape body, float x, float y, float z,
                            float scale, float rot, int id) {
        super(sketch, body, x, y, z, id);
        h = 4240 * scale;
        w = 1420 * scale;
        d = 1420 * scale;
        rotation = rot;

        hb1 = new HitboxObject(sketch, body, x, h, z, id);
        hb1.setHWD(244, 1056, 1076);
        sketch.gameObjects.add(hb1);
        hb2 = new HitboxObject(sketch, body, x, h + 226, z, id);
        hb2.setHWD(226, 712, 732);
        sketch.gameObjects.add(hb2);
        hb3 = new HitboxObject(sketch, body, x, h + 446, z, id);
        hb3.setHWD(220, 110, 110);
        sketch.gameObjects.add(hb3);
        hb4 = new HitboxObject(sketch, body, x, h + 768, z, id);
        hb4.setHWD(600, 34, 34);
        sketch.gameObjects.add(hb4);
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
        sketch.translate(getCoords().x, getCoords().y - getH()/2, getCoords().z);
        sketch.rotateY(rotation);
        sketch.shape(body);
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
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
