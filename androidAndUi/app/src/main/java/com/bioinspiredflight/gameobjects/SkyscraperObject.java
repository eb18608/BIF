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
        h = 2120 * scale;
        w = 710 * scale;
        d = 710 * scale;
        rotation = rot;

        hb1 = new HitboxObject(sketch, body, x, h, z, id);
        hb1.setHWD(122, 528, 538);
        sketch.gameObjects.add(hb1);
        hb2 = new HitboxObject(sketch, body, x, h + 113, z, id);
        hb2.setHWD(113, 356, 366);
        sketch.gameObjects.add(hb2);
        hb3 = new HitboxObject(sketch, body, x, h + 223, z, id);
        hb3.setHWD(110, 55, 55);
        sketch.gameObjects.add(hb3);
        hb4 = new HitboxObject(sketch, body, x, h + 384, z, id);
        hb4.setHWD(300, 17, 17);
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
        sketch.pushMatrix();
        sketch.translate(getCoords().x - getW()/2, getCoords().y - getH()/2, getCoords().z - getD()/2);
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
