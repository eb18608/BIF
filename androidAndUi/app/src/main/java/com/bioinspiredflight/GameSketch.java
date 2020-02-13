package com.bioinspiredflight;

import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
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
    private CollideMod collideMod;
    private final float scale = 1.0f;

    public void setMovingObject(Movement movingObject, ControlMod controlMod, InputToOutput io, CollideMod collideMod){
        this.movingObject = movingObject;
        this.controlMod = controlMod;
        this.io = io;
        this.visitor = new ModVisitor();
        this.collideMod = collideMod;
    }

    public class droneObject {
        float h, di;
        final float scale;
        PVector coords;
        PShape body, propellerFL, propellerFR, propellerBL, propellerBR;

        public droneObject(float diameter, float hei, float x, float y, float z,
                           final float s) {
            h = hei;
            di = diameter;
            coords = new PVector(x, y, z);
            body = loadShape("textured_circular_drone_sans_propellers.obj");
            System.out.printf("Initial depth: %.3f\n", body.getDepth());
            scale = s;
            body.scale(scale);
            System.out.printf("Scaled depth: %.3f\n", body.getDepth());
            propellerFL = loadPropeller(scale);
            propellerFR = loadPropeller(scale);
            propellerBL = loadPropeller(scale);
            propellerBR = loadPropeller(scale);
        }

        private PShape loadPropeller(float scale){
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
            drone.propellerFL.rotateY(multiplier);
            drone.propellerFR.rotateY(-multiplier);
            drone.propellerBL.rotateY(multiplier);
            drone.propellerBR.rotateY(-multiplier);
            popMatrix();
        }
    }

    public class buildingObject {
        public float h, w, d;
        public PVector coords;
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

    public void setCamera(float scale) {
        float eyex = drone.coords.x - (scale * 200 * sin(rotation));
        float eyey = drone.coords.y + (scale * 100);
        float eyez = drone.coords.z - (scale * 200 * cos(rotation));
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

    public float distanceToDrone(buildingObject b) {
        float x = Math.abs(drone.coords.x - b.coords.x);
        float z = Math.abs(drone.coords.z - b.coords.z);
        return (float)Math.sqrt((x*x) + (z*z));
    }

    public float avg(float i, float j) {
        return ((i+j)/2);
    }

    public void setup() {
        frameRate(30);
        drone = new droneObject(105, 16,  0, 0, 0, scale);
        movingObject.setMovementSize(drone);
        float width = 400 * scale;
        float height = 600 * scale;
        float depth = 400 * scale;
        buildings[0] = new buildingObject(width, height, depth, 300, height/2, 300);
        buildings[1] = new buildingObject(width, height, depth, 300, height/2, 720);
        buildings[2] = new buildingObject(width, height, depth, 720, height/2, 300);
        buildings[3] = new buildingObject(width, height, depth, 720, height/2, 720);
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
    }

    public void draw() {
        for (buildingObject b : buildings){
            movingObject.collisionDetectorZ(movingObject, b);
            movingObject.collisionDetectorXY(movingObject, b);
            movingObject.isCollision(movingObject, b);

            System.out.println("collided? " + movingObject.collided);
            System.out.println("building position: " + b.coords);
            System.out.println("drone position: "+ movingObject.getPos());
        }
        System.out.println(width);
        controlMod.accept(visitor, movingObject);

        float droneLeftRight = movingObject.getX(movingObject.getPos());
        float droneForwardBack = movingObject.getY(movingObject.getPos());
        float droneUpDown = movingObject.getZ(movingObject.getPos());

        // 3D Section
        background(100);
        lights();
        drone.spinPropellers(0.3f);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera(scale);

        for (buildingObject b : buildings) {
            pushMatrix();
            translate(b.coords.x - b.w/2,
                    b.coords.y - b.h/2, b.coords.z - b.d/2);
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

        translate(width - 160, 160);

        fill(153);
        circle(0, 0, 300);

        pushMatrix();
        rotate(-rotation);
        pushMatrix();
        translate(-drone.coords.x/10, drone.coords.z/10);
        for (buildingObject b : buildings) {
            if (distanceToDrone(b) + avg(b.w/2, b.d/2) < 1500) {
                pushMatrix();
                translate(b.coords.x/10 - b.w/20, -b.coords.z/10 - b.d/20);
                fill(200);
                rect(0, 0, b.w/10, b.d/10);
                popMatrix();
            }
        }
        popMatrix();
        popMatrix();
        fill(0);
        image(droneIcon, -drone.di/15, -drone.di/15, drone.di/7.5f, drone.di/7.5f);

        hint(ENABLE_DEPTH_TEST);
    }
    public void settings() {
        fullScreen(P3D);

    }
}
