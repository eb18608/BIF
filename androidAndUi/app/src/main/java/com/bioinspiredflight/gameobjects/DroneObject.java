package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.sensor.SensorContent;
import com.bioinspiredflight.gameobjects.GameObjectList;

import java.lang.reflect.Array;
import java.util.ArrayList;

import processing.core.PShape;
import processing.core.PVector;

public class DroneObject extends GameObject {
    public float di;    //keeping this public for optimization reasons
    float scale;
    float lrTilt = 0;
    float fbTilt = 0;
    float tiltMult = 0.0002f;
    float tiltMax = 0.2f;
    PShape propellerFL, propellerFR, propellerBL, propellerBR, arrow;
    InputToOutput io;
    float collectibleRotation = sketch.PI;
    float flipRotation;
    ArrayList<PShape> sensorBodies = new ArrayList<>();

    public DroneObject(GameSketch sketch, PShape body, float x, float y, float z,
                       final float s, int id) {
        super(sketch, body, x, y, z, id);
        h =  23 * s;
        di = 105 * s;
        scale = s;
        body.scale(scale);
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);
        arrow = sketch.loadShape("arrow.obj");
        this.arrow.setFill(sketch.color(150,0,0,150));

        for (SensorContent.SensorItem sensor : SensorContent.ITEMS) {
            PShape tempBody = sketch.loadShape(sensor.getBodyfilePath());
            sensorBodies.add(tempBody);
        }
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
        sketch.rotateY(calculateAngle(acc.x, acc.y));
        flipRotation += sketch.PI/10;
        sketch.rotateX(flipRotation);
        sketch.rotateY(-calculateAngle(acc.x, acc.y));
    }

    private float calculateAngle(float x, float y) {
        float theta = (float) Math.atan(Math.abs(x/y));
        if (x >= 0) {
            if (y >= 0) { // x+ve, y+ve
                return theta;
            } else { // x+ve, y-ve
                return sketch.PI - theta;
            }
        } else {
            if (y >= 0) { // x-ve, y+ve
                return -theta;
            } else { // x-ve, y-ve
                return sketch.PI + theta;
            }
        }
    }

    private GameObject trackingObject() {
        GameObject closest = this;
        float distance = 100000000;
        for (GameObject object : sketch.gameObjects.getObjectiveList()) {
            if (object.shouldBeTracked() && sketch.distanceToDrone(object) < distance) {
                distance = sketch.distanceToDrone(object);
                closest = object;
            }
        }
        return closest;
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

    public void drawArrow() {
        sketch.pushMatrix();
        sketch.translate(0,-30,0);
        sketch.rotateY(-calculateAngle(trackingObject().coords.z - this.coords.z, trackingObject().coords.x - this.coords.x) + sketch.PI/2);
        sketch.shape(arrow);
        sketch.popMatrix();
    }

    @Override
    public void draw3D() {
        final float propellerXZ = 22f * this.scale;
        final float propellerY = 2f * this.scale;

        sketch.pushMatrix();

        sketch.shape(body);

        int i = 0;
        for (SensorContent.SensorItem sensor : SensorContent.ITEMS) {
            if (sensor.isEquipped()) {
                sketch.shape(sensorBodies.get(i));
            }
            i++;
        }

        for (int j = 1; j <= sketch.getCollectiblesHeld(); j++) {
            sketch.pushMatrix();
            sketch.translate(0, 30 * j, 0);
            sketch.rotateX(sketch.PI/2);
            sketch.rotateZ(collectibleRotation + (sketch.PI * (j-1) / 8));
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
