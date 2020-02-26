package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.Movement;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import java.lang.Math;

public class DroneObject extends GameObject {
    float h;
    public float di;    //keeping this public for optimization reasons
    final float scale;
    float lrTilt = 0;
    float fbTilt = 0;
    PShape propellerFL, propellerFR, propellerBL, propellerBR;


    public DroneObject(PApplet sketch, float diameter, float hei, float x, float y, float z,
                       final float s, String droneFilename) {
        super(sketch, x, y, z, droneFilename);
        h = hei;
        di = diameter;
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

    public void draw(Movement movingObject) {
        final float propellerXZ = 22f * this.scale;
        final float propellerY = 2f * this.scale;
        PVector vel = movingObject.getVel();
        PVector acc = movingObject.getAcc();
        sketch.pushMatrix();
        if (vel.y != 0) {
            if ((Math.abs(sketch.PI / 3200 * acc.y) < Math.abs((vel.y * 0.01f))) && ((vel.y > 0 && acc.y > 0) || (vel.y < 0 && acc.y < 0))) {
                fbTilt = sketch.PI / 3200 * acc.y;
                System.out.print("maxtilt fb: ");
                System.out.println(fbTilt);
            } else {
                fbTilt = vel.y * 0.01f;
                System.out.print("veltilt fb: ");
                System.out.println(fbTilt);
            }
        }
        sketch.rotateX(fbTilt);
        if (vel.x != 0) {
            if (Math.abs(sketch.PI / 3200 * acc.x) < Math.abs((vel.x * 0.01f)) && ((vel.x > 0 && acc.x > 0) || (vel.x < 0 && acc.x < 0))) {
                lrTilt = -sketch.PI / 3200 * acc.x;
                System.out.print("maxtilt lr: ");
                System.out.println(lrTilt);
            } else {
                lrTilt = -vel.x * 0.01f;
                System.out.print("lrltilt lr: ");
                System.out.println(fbTilt);
            }
        }
        sketch.rotateZ(lrTilt);

        sketch.shape(body);

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

    public void spinPropellers(float multiplier) {
        sketch.pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(multiplier);
        this.propellerBR.rotateY(-multiplier);
        sketch.popMatrix();
    }
}
