package com.bioinspiredflight;

import processing.core.PShape;
import processing.core.PVector;

public class DroneObject extends GameObject {
    float h, di;
    final float scale;
    PVector coords;
    PShape body, propellerFL, propellerFR, propellerBL, propellerBR;


    public DroneObject(float diameter, float hei, float x, float y, float z,
                       final float s, String droneFilename) {
        h = hei;
        di = diameter;
        coords = new PVector(x, y, z);
        body = loadShape(droneFilename);
//            System.out.printf("Initial depth: %.3f\n", body.getDepth());
        scale = s;
        body.scale(scale);
//            System.out.printf("Scaled depth: %.3f\n", body.getDepth());
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);
    }

    private PShape loadPropeller(float scale) {
        PShape shape = loadShape("textured_propeller.obj");
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

    public void draw() {
        final float propellerXZ = 22f * this.scale;
        final float propellerY = 2f * this.scale;

        shape(body);

        pushMatrix();
        translate(-propellerXZ, propellerY, propellerXZ);
        shape(propellerFL);
        popMatrix();

        pushMatrix();
        translate(propellerXZ, propellerY, propellerXZ);
        shape(propellerFR);
        popMatrix();

        pushMatrix();
        translate(-propellerXZ, propellerY, -propellerXZ);
        shape(propellerBL);
        popMatrix();

        pushMatrix();
        translate(propellerXZ, propellerY, -propellerXZ);
        shape(propellerBR);
        popMatrix();
    }

    public void spinPropellers(float multiplier) {
        pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(multiplier);
        this.propellerBR.rotateY(-multiplier);
        popMatrix();
    }
}
