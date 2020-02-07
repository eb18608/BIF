package com.bioinspiredflight;

import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitable;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

/**
 * This is a placeholder class for Processing code.
 * The game is going to run from here.
 */
public class GameSketch extends PApplet{

    private Movement movingObject;
    private ControlMod controlMod;
    private InputToOutput io;
    private ModVisitor visitor;
    private final float rotationSpeed = -0.1f;  // a positive value is inverted

    public void setMovingObject(Movement movingObject, ControlMod controlMod, InputToOutput io){
        this.movingObject = movingObject;
        this.controlMod = controlMod;
        this.io = io;
        this.visitor = new ModVisitor();
    }

    public class droneObject {
        private float h, w, d;
        private PVector coords;
        private PShape hitbox, body, propellerFL, propellerFR, propellerBL, propellerBR;

        public droneObject(float wid, float hei, float dep, float x, float y, float z) {
            h = hei;
            w = wid;
            d = dep;
            coords = new PVector(x, y, z);
            hitbox = createShape(BOX, w, h, d);
            body = loadShape("textured_circular_drone_sans_propellers.obj");
            propellerFL = loadShape("textured_propeller.obj");
            propellerFR = loadShape("textured_propeller.obj");
            propellerBL = loadShape("textured_propeller.obj");
            propellerBR = loadShape("textured_propeller.obj");
        }

        public float getHeight() { return h; }
        public float getWidth() { return w; }
        public float getDepth() { return d; }
        public PVector getCoords() { return coords; }

        public void move(float x, float y, float z) {
            coords.add(x, y, z);
        }

        public void draw() {
            shape(hitbox);
            shape(body);

            pushMatrix();
            translate(-22, 2, 22);
            shape(propellerFL);
            popMatrix();

            pushMatrix();
            translate(22, 2, 22);
            shape(propellerFR);
            popMatrix();

            pushMatrix();
            translate(-22, 2, -22);
            shape(propellerBL);
            popMatrix();

            pushMatrix();
            translate(22, 2, -22);
            shape(propellerBR);
            popMatrix();
        }

        public void spinPropellers(float multiplier) {
            pushMatrix();
            drone.propellerFL.rotateY(multiplier);
            drone.propellerFR.rotateY(-multiplier);
            drone.propellerBL.rotateY(multiplier);
            drone.propellerBR.rotateY(-multiplier);
            popMatrix();
        }
    }

    public class buildingObject {
        private float h, w, d;
        private PVector coords;

        public buildingObject(float wid, float hei, float dep, float x, float y, float z) {
            h = hei;
            w = wid;
            d = dep;
            coords = new PVector(x, y, z);
        }

        public float getHeight() { return h; }
        public float getWidth() { return w; }
        public float getDepth() { return d; }
        public PVector getCoords() { return coords; }
    }

    PImage texture;
    droneObject drone;
    buildingObject building;
    float rotation;

    public void setCamera() {
        float eyex = drone.coords.x - (200 * sin(rotation));
        float eyey = drone.coords.y + 100;
        float eyez = drone.coords.z - (200 * cos(rotation));
        camera(eyex, eyey, eyez, drone.coords.x, drone.coords.y, drone.coords.z, 0, -1, 0);
    }

    public void renderBuilding(float wid, float hei, float dep) {
        beginShape(QUADS);
        texture = loadImage("SkyscraperFront.png");
        texture(texture);
        //frontface
        vertex(0, 0, 0, 1, 1);
        vertex(0, 0, dep, 0, 1);
        vertex(0, hei, dep, 0, 0);
        vertex(0, hei, 0, 1, 0);
        //backface
        vertex(wid, 0, dep, 1, 1);
        vertex(wid, 0, 0, 0, 1);
        vertex(wid, hei, 0, 0, 0);
        vertex(wid, hei, dep, 1, 0);
        //leftface
        vertex(wid, 0, 0, 1, 1);
        vertex(0, 0, 0, 0, 1);
        vertex(0, hei, 0, 0, 0);
        vertex(wid, hei, 0, 1, 0);
        //rightface
        vertex(0, 0, dep, 1, 1);
        vertex(wid, 0, dep, 0, 1);
        vertex(wid, hei, dep, 0, 0);
        vertex(0, hei, dep, 1, 0);
        //topface
        vertex(0, hei, 0);
        vertex(0, hei, dep);
        vertex(wid, hei, dep);
        vertex(wid, hei, 0);
        endShape();
    }

    public void setup() {
        frameRate(30);
        drone = new droneObject(77, 16, 77, 0, 0, 100);
        building = new buildingObject(400, 600, 400, 200, 0, 50);
        textureMode(NORMAL);
    }

    public void draw() {
        controlMod.accept(visitor, movingObject);
        float droneLeftRight = (float) movingObject.getVelX();
        float droneForwardBack = (float) movingObject.getVelY();
        float droneUpDown = (float) movingObject.getVelZ();
        background(100);
        lights();
        drone.spinPropellers(0.3f);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera();

        pushMatrix();
        translate(building.getCoords().x, building.getCoords().y, building.getCoords().z);
        renderBuilding(building.getWidth(), building.getHeight(), building.getDepth());
        popMatrix();

        pushMatrix();
        rotation += io.getRotation() * rotationSpeed;
        io.setTotalRotation(-rotation);
        translate(drone.getCoords().x, drone.getCoords().y, drone.getCoords().z);
        rotateY(rotation);
        drone.draw();
        rotateY(-rotation);
        popMatrix();

        drone.hitbox.setVisible(false);

    }
    public void settings() {
        size(1600, 900, P3D);

    }
}
