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
        float h, di;
        PVector coords;
        PShape body, propellerFL, propellerFR, propellerBL, propellerBR;

        public droneObject(float hei, float diameter, float x, float y, float z) {
            h = hei;
            di = diameter;
            coords = new PVector(x, y, z);
            body = loadShape("textured_circular_drone_sans_propellers.obj");
            propellerFL = loadShape("textured_propeller.obj");
            propellerFR = loadShape("textured_propeller.obj");
            propellerBL = loadShape("textured_propeller.obj");
            propellerBR = loadShape("textured_propeller.obj");
        }

        public void move(float x, float y, float z) {
            coords.add(x, y, z);
        }

        public void draw() {
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
        float h, w, d;
        PVector coords;
        PShape body;

        public buildingObject(float wid, float hei, float dep, float x, float y, float z) {
            h = hei;
            w = wid;
            d = dep;
            coords = new PVector(x, y, z);
            body = loadShape("textured_drone_sans_propellers.obj");
        }

        public void draw() {
            shape(body);
        }
    }

    PImage texture;
    PImage droneIcon;
    droneObject drone;
    buildingObject[] buildings = new buildingObject[4];
    float rotation;
    int[] minimapCoords = {1440, 160};

    public void setCamera() {
        float eyex = drone.coords.x - (200 * sin(rotation));
        float eyey = drone.coords.y + 100;
        float eyez = drone.coords.z - (200 * cos(rotation));
        camera(eyex, eyey, eyez, drone.coords.x, drone.coords.y, drone.coords.z, 0, -1, 0);
    }

    public void renderBuilding(float wid, float hei, float dep) {
        beginShape(QUADS);
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
        drone = new droneObject(15, 105, 0, 0, 0);
        buildings[0] = new buildingObject(400, 600, 400, 300, 300, 300);
        buildings[1] = new buildingObject(400, 600, 400, 300, 300, 720);
        buildings[2] = new buildingObject(400, 600, 400, 720, 300, 300);
        buildings[3] = new buildingObject(400, 600, 400, 720, 300, 720);
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
    }

    public void draw() {
        controlMod.accept(visitor, movingObject);
        float droneLeftRight = (float) movingObject.getVelX();
        float droneForwardBack = (float) movingObject.getVelY();
        float droneUpDown = (float) movingObject.getVelZ();

        // 3D Section
        background(100);
        lights();
        drone.spinPropellers(0.3f);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera();

        for (buildingObject b : buildings) {
            pushMatrix();
            translate(b.coords.x - b.w/2, b.coords.y - b.h/2, b.coords.z - b.d/2);
            renderBuilding(b.w, b.h, b.d);
            //buildings[i].draw();
            popMatrix();
        }

        pushMatrix();
        rotation += io.getRotation() * rotationSpeed;
        io.setTotalRotation(-rotation);
        translate(drone.coords.x, drone.coords.y, drone.coords.z);
        rotateY(rotation);
        drone.draw();
        rotateY(-rotation);
        popMatrix();

        // 2D Section
        camera();
        hint(DISABLE_DEPTH_TEST);


        translate(minimapCoords[0], minimapCoords[1]);
        fill(153);
        circle(0, 0, 300);
        fill(200);
        pushMatrix();
        rotate(-rotation);
        pushMatrix();
        translate(-drone.coords.x/10, drone.coords.z/10);
        for (buildingObject b : buildings) {
            pushMatrix();
            translate(b.coords.x/10 - b.w/20, -b.coords.z/10 - b.d/20);
            rect(0, 0, b.w/10, b.d/10);
            popMatrix();
        }
        popMatrix();
        popMatrix();
        fill(204, 102, 0, 100);
        arc(0, 0, 300, 300, rotation - (3 *PI)/4, rotation - PI/4);
        fill(0);
        image(droneIcon, -drone.di/20, -drone.di/20, drone.di/10, drone.di/10);
        hint(ENABLE_DEPTH_TEST);
    }
    public void settings() {
        size(1600, 900, P3D);

    }
}
