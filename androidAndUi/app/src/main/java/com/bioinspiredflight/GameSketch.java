package com.bioinspiredflight;

import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;

import processing.core.PApplet;
import processing.core.PImage;
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
    private PVector lastNonCollision = new PVector();

    public PVector getLastPosition(){ return lastNonCollision; }

    public void setLastPosition(PVector pos) {this.lastNonCollision = pos;}
    public void setMovingObject(Movement movingObject, ControlMod controlMod, InputToOutput io, CollideMod collideMod){
        this.movingObject = movingObject;
        this.controlMod = controlMod;
        this.io = io;
        this.visitor = new ModVisitor();
        this.collideMod = collideMod;
    }

    PImage texture;
    PImage droneIcon;
    DroneObject drone;
    BuildingObject[] buildings = new BuildingObject[4];
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

    public float distanceToDrone(BuildingObject b) {
        float x = Math.abs(drone.coords.x - b.getCoords().x);
        float z = Math.abs(drone.coords.z - b.getCoords().z);
        return (float)Math.sqrt((x*x) + (z*z));
    }

    public float avg(float i, float j) {
        return ((i+j)/2);
    }

    public void setup() {
        frameRate(30);
        drone = new DroneObject(this, 105, 16,  0, 0, 0, scale, "textured_circular_drone_sans_propellers.obj");
        movingObject.setMovementSize(drone);
        float width = 400 * scale;
        float height = 600 * scale;
        float depth = 400 * scale;
        buildings[0] = new BuildingObject(this, width, height, depth, 300, height/2, 300, "textured_drone_sans_propellers.obj");
        buildings[1] = new BuildingObject(this, width, height, depth, 300, height/2, 720, "textured_drone_sans_propellers.obj");
        buildings[2] = new BuildingObject(this, width, height, depth, 720, height/2, 300, "textured_drone_sans_propellers.obj");
        buildings[3] = new BuildingObject(this, width, height, depth, 720, height/2, 720, "textured_drone_sans_propellers.obj");
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
    }

    public void draw3d(float droneLeftRight, float droneUpDown, float droneForwardBack){
        // 3D Section
        background(100);
        lights();
        drone.spinPropellers(0.3f);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera(scale);

        for (BuildingObject bd : buildings) {
            pushMatrix();
            translate(bd.getCoords().x - bd.getW()/2,
                    bd.getCoords().y - bd.getH()/2, bd.getCoords().z - bd.getD()/2);
            renderBuilding(bd.getW(), bd.getH(), bd.getD());
            //buildings[i].draw();
            popMatrix();
        }

        pushMatrix();
        rotation += io.getRotation() * rotationSpeed;
        io.setTotalRotation(-rotation);
        translate(drone.coords.x, drone.coords.y, drone.coords.z);
        rotateY(rotation);
        drone.draw(movingObject);
        rotateY(-rotation);
        popMatrix();
    }

    public void draw2d(){
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
        for (BuildingObject b : buildings) {
            if (distanceToDrone(b) + avg(b.getW()/2, b.getD()/2) < 1500) {
                pushMatrix();
                translate(b.getCoords().x/10 - b.getW()/20, -b.getCoords().z/10 - b.getD()/20);
                fill(200);
                rect(0, 0, b.getW()/10, b.getD()/10);
                popMatrix();
            }
        }
        popMatrix();
        popMatrix();
        fill(0);
        image(droneIcon, -drone.di/15, -drone.di/15, drone.di/7.5f, drone.di/7.5f);

        hint(ENABLE_DEPTH_TEST);
    }

    public void draw() {


        for (int b = 0; b < buildings.length && movingObject.collided == false ; b++) {
                movingObject.collisionDetectorZ(movingObject, buildings[b]);
                movingObject.collisionDetectorXY(movingObject, buildings[b]);
                movingObject.isCollision(movingObject, buildings[b]);
//                System.out.println("collided? " + movingObject.collided);
//                System.out.println("building position: " + buildings[b].coords);
//                System.out.println("drone position: " + movingObject.getPos());
        }

        if(movingObject.collided == true){
//            System.out.println("CollideMod's Saved Position!!!: " + lastNonCollision);
            collideMod.accept(visitor, movingObject, this);

            movingObject.collided = false;
        } else {
            setLastPosition(movingObject.getPos());
        }

//        System.out.println(width);
        controlMod.accept(visitor, movingObject);

//        System.out.println("Saved Position: "+ lastNonCollision);

        float droneLeftRight = movingObject.getX(movingObject.getPos());
        float droneForwardBack = movingObject.getY(movingObject.getPos());
        float droneUpDown = movingObject.getZ(movingObject.getPos());

        draw3d(droneLeftRight, droneUpDown, droneForwardBack);

        draw2d();

    }
    public void settings() {
        fullScreen(P3D);

    }
}
