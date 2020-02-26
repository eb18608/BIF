package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.ui.Joystick;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import java.lang.Math;

public class DroneObject extends GameObject {
    //float h;
    public float di;    //keeping this public for optimization reasons
    final float scale;
    float lrTilt = 0;
    float fbTilt = 0;
    PShape propellerFL, propellerFR, propellerBL, propellerBR;
    InputToOutput io;

    public DroneObject(GameSketch sketch, PShape body, float x, float y, float z,
                       final float s) {
        super(sketch, body, x, y, z);
        //objectFileName = "textured_circular_drone_sans_propellers.obj";
        //loadShape();
        h =  16;
        di = 105;
//            System.out.printf("Initial depth: %.3f\n", body.getDepth());
        scale = s;
        body.scale(scale);
//            System.out.printf("Scaled depth: %.3f\n", body.getDepth());
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);
    }

    public DroneObject(GameSketch sketch, PShape body, float diameter, float hei, float x, float y, float z,
                       final float s) {
        super(sketch, body, x, y, z);
        objectFileName = "textured_circular_drone_sans_propellers.obj";
        //loadShape();
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

    public void setInputToOutput(InputToOutput io){
        this.io = io;
    }

    @Override
    public void draw3D() {
        this.spinPropellers(0.3f);

        final float propellerXZ = 22f * this.scale;
        final float propellerY = 2f * this.scale;
        PVector acc = sketch.getMovingObject().getAcc();
        float tiltMult = sketch.PI / 24000;
        float tiltMax = sketch.PI / 32;

        sketch.pushMatrix();
        if (io.isUsingJoystick()) {
            if ((acc.x > 0) && (tiltMax > lrTilt)) {
                lrTilt += tiltMult * acc.x;
            } else if ((acc.x < 0) && (-tiltMax < lrTilt)) {
                lrTilt += tiltMult * acc.x;
            }
            sketch.rotateZ(-lrTilt);

            if ((acc.y > 0) && (tiltMax > fbTilt)) {
                fbTilt += tiltMult * acc.y;
            } else if ((acc.y < 0) && (-tiltMax < fbTilt)) {
                fbTilt += tiltMult * acc.y;
            }
            sketch.rotateX(fbTilt);
        }
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

    @Override
    public void draw2D() {

    }

    public void spinPropellers(float multiplier) {
        sketch.pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(multiplier);
        this.propellerBR.rotateY(-multiplier);
        sketch.popMatrix();
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {

    }

    @Override
    public boolean isDrone() {
        return true;
    }
}
