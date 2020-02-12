package com.bioinspiredflight;

import com.bioinspiredflight.physics.CollideMod;
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
    private CollideMod collideMod;

    public void setMovingObject(Movement movingObject, ControlMod controlMod, InputToOutput io, CollideMod collideMod){
        this.movingObject = movingObject;
        this.controlMod = controlMod;
        this.io = io;
        this.visitor = new ModVisitor();
        this.collideMod = collideMod;
    }

    public class droneObject {
        float h, w, d;
        PVector coords;
        PShape hitbox, body, propellerFL, propellerFR, propellerBL, propellerBR;

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

        public void move(float x, float y, float z) {
            coords.add(x, y, z);
        }

        public float getH() {
            return h;
        }

        public float getW() {
            return w;
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
        drone = new droneObject(77, 16, 77, 0, 0, 0);
        movingObject.setMovementSize(drone);
        buildings[0] = new buildingObject(400, 600, 400, 300, 300, 300);
        buildings[1] = new buildingObject(400, 600, 400, 300, 300, 720);
        buildings[2] = new buildingObject(400, 600, 400, 720, 300, 300);
        buildings[3] = new buildingObject(400, 600, 400, 720, 300, 720);
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
    }

    public void draw() {
        int b;
        for (b = 0; b < buildings.length ; b++){
            movingObject.collisionDetectorZ(movingObject, buildings[b]);
            movingObject.collisionDetectorXY(movingObject, buildings[b]);
            movingObject.isCollision(movingObject, buildings[b]);

            System.out.println("collided? " + movingObject.collided);
            System.out.println("building position: " + buildings[b].coords);
            System.out.println("drone position: "+ movingObject.getPos());
        }

        controlMod.accept(visitor, movingObject);

        float droneLeftRight = movingObject.getX(movingObject.getVel());
        float droneForwardBack = movingObject.getY(movingObject.getVel());
        float droneUpDown = movingObject.getZ(movingObject.getVel());

        // 3D Section
        background(100);
        lights();
        drone.spinPropellers(0.3f);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera();

        for (int i = 0; i < buildings.length; i++) {
            pushMatrix();
            translate(buildings[i].coords.x - buildings[i].w/2,
                    buildings[i].coords.y - buildings[i].h/2, buildings[i].coords.z - buildings[i].d/2);
            renderBuilding(buildings[i].w, buildings[i].h, buildings[i].d);
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

        drone.hitbox.setVisible(false);

        // 2D Section
        camera();
        hint(DISABLE_DEPTH_TEST);

        fill(153);
        circle(minimapCoords[0], minimapCoords[1], 300);
        fill(204, 102, 0, 100);
        arc(minimapCoords[0], minimapCoords[1], 300, 300, rotation - (3 *PI)/4, rotation - PI/4);
        fill(0);
        hint(ENABLE_DEPTH_TEST);
    }
    public void settings() {
        size(1600, 900, P3D);

    }
}
