package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;

import processing.core.PShape;
import processing.core.PVector;

public class DroneObject extends GameObject {
    public float di;    //keeping this public for optimization reasons
    final float scale;
    float lrTilt = 0;
    float fbTilt = 0;
    float tiltMult = 0.0002f;
    float tiltMax = 0.2f;
    PShape propellerFL, propellerFR, propellerBL, propellerBR;
    InputToOutput io;
    float collectibleRotation = sketch.PI;
    float flipRotation;

    public DroneObject(GameSketch sketch, PShape body, float x, float y, float z,
                       final float s, int id) {
        super(sketch, body, x, y, z, id);
        h =  17 * s;
        di = 105 * s;
        scale = s;
        body.scale(scale);
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);
    }

    private PShape loadPropeller(float scale) {
        PShape shape = sketch.loadShape("textured_propeller.obj");
        shape.scale(scale);
        return shape;
    }

    public void move(float x, float y, float z) {
        coords.x = x;
        coords.y = y;
        coords.z = z;
    }

    public float getH() {
        return h;
    }

    public float getDi() {
        return di;
    }

    public void setInputToOutput(InputToOutput io){
        this.io = io;
    }

    public void flipDrone(PVector acc) {
        System.out.println(acc);
        sketch.rotateY(calculateFlipAngle(acc));
        flipRotation += sketch.PI/10;
        sketch.rotateX(flipRotation);
        sketch.rotateY(-calculateFlipAngle(acc));
    }

    private float calculateFlipAngle(PVector acc) {
        acc.set(sketch.round(acc.x), sketch.round(acc.y), sketch.round(acc.z));
        float theta = (float) Math.atan(Math.abs(acc.x/acc.y));
        System.out.println(theta);
        if (acc.x >= 0) {
            if (acc.y >= 0) { // x+ve, y+ve
                return theta;
            } else { // x+ve, y-ve
                return sketch.PI - theta;
            }
        } else {
            if (acc.y >= 0) { // x-ve, y+ve
                return -theta;
            } else { // x-ve, y-ve
                return sketch.PI + theta;
            }
        }
    }

    public void tiltDrone(PVector acc) {
        acc.set(sketch.round(acc.x), sketch.round(acc.y), sketch.round(acc.z));
        if (io.isUsingJoystick()) {
            if ((acc.x > 0) && (tiltMax > lrTilt)) {
                lrTilt += tiltMult * acc.x;
            } else if ((acc.x < 0) && (-tiltMax < lrTilt)) {
                lrTilt += tiltMult * acc.x;
            }

            if ((acc.y > 0) && (tiltMax > fbTilt)) {
                fbTilt += tiltMult * acc.y;
            } else if ((acc.y < 0) && (-tiltMax < fbTilt)) {
                fbTilt += tiltMult * acc.y;
            }

        } else {
            if (lrTilt > 0) {
                lrTilt -= tiltMult * 250;
                if (lrTilt < 0) { lrTilt = 0; }
            } else if (lrTilt < 0) {
                lrTilt += tiltMult * 250;
                if (lrTilt > 0) { lrTilt = 0; }
            }

            if (fbTilt > 0) {
                fbTilt -= tiltMult * 250;
                if (fbTilt < 0) { fbTilt = 0; }
            } else if (fbTilt < 0) {
                fbTilt += tiltMult * 250;
                if (fbTilt > 0) { fbTilt = 0; }
            }
        }
        sketch.rotateZ(-lrTilt);
        sketch.rotateX(fbTilt);
    }

    @Override
    public void draw3D() {
        final float propellerXZ = 22f * this.scale;
        final float propellerY = 2f * this.scale;

        sketch.pushMatrix();

        sketch.shape(body);

        if (sketch.getHoldingCollectible()) {
            sketch.pushMatrix();
            sketch.translate(0, 30, 0);
            sketch.rotateX(sketch.PI/2);
            sketch.rotateZ(collectibleRotation);
            collectibleRotation += 0.025f;
            sketch.shape(sketch.getCollectibleShape());
            sketch.popMatrix();
        }

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
    }

    @Override
    public void draw2D() { }

    public void spinPropellers(float multiplier) {
        sketch.pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(-multiplier);
        this.propellerBR.rotateY(multiplier);
        sketch.popMatrix();
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) { }

    @Override
    public boolean isDrone() {
        return true;
    }
}
