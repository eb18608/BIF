package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class SearchlightObject extends GameObject {
    PShape lightHolderBody, propellerFL, propellerFR, propellerBL, propellerBR;
    int initx, finx, initz, finz;
    int fbx = 1;
    int fbz = 1;
    HitboxObject lightHolderHitbox;

    public SearchlightObject(GameSketch sketch, PShape body, float x, float y, float z,
                             float scale, int id, int finx, int finz) {
        super(sketch, body, (int)x, y, (int)z, id);
        if (SensorContent.ITEMS.get(2).isEquipped()) {
            scale *= 0.75f;
        }
        h = 400;
        w = 400 * scale;
        d = 400 * scale;

        this.initx = (int) x;
        this.finx = finx;
        this.initz = (int) z;
        this.finz = finz;

        if (finx < x) { fbx = -1; }
        if (finz < z) { fbz = -1; }

        lightHolderBody = sketch.loadShape("textured_drone_sans_propellers.obj");
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);

        lightHolderHitbox = new HitboxObject(sketch, body, x, h + 8.5f, z, id);
        lightHolderHitbox.setHWD(17, 100, 100);
        sketch.gameObjects.add(lightHolderHitbox);
        this.setSolid(false);
    }

    private PShape loadPropeller(float scale) {
        PShape shape = sketch.loadShape("textured_propeller.obj");
        shape.scale(scale);
        return shape;
    }

    public PVector getCoords(){ return this.coords; }

    public float getH(){ return h; }

    public float getW() { return w; }

    public float getD() { return d; }

    private void spinProps(float multiplier) {
        sketch.pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(-multiplier);
        this.propellerBR.rotateY(multiplier);
        sketch.popMatrix();
    }

    private void move() {
        this.coords.x += fbx;
        this.coords.z += fbz;
        lightHolderHitbox.coords.x += fbx;
        lightHolderHitbox.coords.z += fbz;
        if (this.getCoords().x == finx || this.getCoords().x == initx) { fbx *= -1; }
        if (this.getCoords().z == finz || this.getCoords().z == initz) { fbz *= -1; }
    }

    private void moveAtSpeed(int speed) {
        for (int i = 0; i < speed; i++) {
            move();
        }
    }

    @Override
    public void draw3D() {
        final float propellerXZ = 22;
        final float propellerY = 2;

        if (initx != finx || initz != finz) {
            moveAtSpeed(3);
        }

        spinProps(0.3f);

        sketch.pushMatrix();

        sketch.translate(getCoords().x, getCoords().y, getCoords().z);
        //sketch.shape(body);

        sketch.pushMatrix();
        sketch.translate(0, getH()/2 + 10, 0);
        sketch.shape(lightHolderBody);

        sketch.pushMatrix();
        sketch.translate(-propellerXZ, propellerY, propellerXZ);
        sketch.shape(propellerFL);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(propellerXZ, propellerY, propellerXZ);
        sketch.shape(propellerFR);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(-propellerXZ, propellerY, -propellerXZ);
        sketch.shape(propellerBL);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(propellerXZ, propellerY, -propellerXZ);
        sketch.shape(propellerBR);
        sketch.popMatrix();
        sketch.popMatrix();

        // Temp building to show location lol
        sketch.translate(-getW()/2,  -getH()/2,  -getD()/2);
        sketch.tint(255, 100);
        sketch.renderBuilding(this.getW(), this.getH(), this.getD());
        sketch.noTint();

        sketch.popMatrix();

    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        //sketch.setLastPosition(movement.getPos());
        //TODO: Game over. "You were seen!"
    }

    @Override
    public boolean isDrone() {
        return false;
    }

    @Override
    public boolean isSearchlight() { return true; }

}
