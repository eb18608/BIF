package com.bioinspiredflight;

import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.utilities.LevelHandler;

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
    private PVector lastNonCollision = new PVector();
    private LevelHandler levelHandler;
    private GameObjectList gameObjects = new GameObjectList();

    private PShape droneBodyShape;
    private PShape buildingShape;
    private PShape staticLoopShape;
    private PShape movingLoopShape;

    public Movement getMovingObject() { return movingObject; }
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

    public float distanceToDrone(GameObject b) {
        float x = Math.abs(drone.coords.x - b.getCoords().x);
        float z = Math.abs(drone.coords.z - b.getCoords().z);
        return (float)Math.sqrt((x*x) + (z*z));
    }

    public float avg(float i, float j) {
        return ((i+j)/2);
    }

    public void setup() {
        frameRate(30);
        droneBodyShape = loadShape("textured_circular_drone_sans_propellers.obj");
        buildingShape = loadShape("simple_helipad.obj");
        /*staticLoopShape = loadShape("loop.obj");
        staticLoopShape.setFill(color(252, 186, 3, 255));
        movingLoopShape = loadShape("textured_circular_drone.obj");
        movingLoopShape.setFill(color(150, 255, 255, 75));*/

        levelHandler.changeLevel(this, gameObjects, "levels/level0.csv");
        drone = gameObjects.getDrone();
        movingObject.setMovementSize(drone);

        drone.setInputToOutput(io);

        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
    }

    public void draw3d(float droneLeftRight, float droneUpDown, float droneForwardBack){
        // 3D Section
        background(100);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera(scale);

        gameObjects.drawNonDroneGameObjects3D();

        pushMatrix();
        rotation += io.getRotation() * rotationSpeed;
        io.setTotalRotation(-rotation);
        translate(drone.coords.x, drone.coords.y, drone.coords.z);
        drone.tiltDrone(movingObject.getAcc());
        rotateY(rotation);
        drone.draw3D();
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
        //draw object icons here
        gameObjects.drawAllGameObjects2D();
        popMatrix();
        popMatrix();
        fill(0);
        image(droneIcon, -drone.di/15, -drone.di/15, drone.di/7.5f, drone.di/7.5f);

        hint(ENABLE_DEPTH_TEST);
    }

    public void draw() {

        gameObjects.checkForCollisions(movingObject);

        if(movingObject.collided == true){
            collideMod.accept(visitor, movingObject, this, gameObjects.get(1));
            movingObject.collided = false;
        } else {
            setLastPosition(movingObject.getPos());
        }

        controlMod.accept(visitor, movingObject);

        float droneLeftRight = movingObject.getX(movingObject.getPos());
        float droneForwardBack = movingObject.getY(movingObject.getPos());
        float droneUpDown = movingObject.getZ(movingObject.getPos());

        draw3d(droneLeftRight, droneUpDown, droneForwardBack);
        draw2d();
    }
    public void settings() {
        fullScreen(P3D);
    }

    public void setLevelHandler(LevelHandler levelHandler){
        this.levelHandler = levelHandler;
    }

    public PShape getDroneBodyShape() {
        return droneBodyShape;
    }

    public void setDroneBodyShape(PShape droneBodyShape) {
        this.droneBodyShape = droneBodyShape;
    }

    public PShape getBuildingShape() {
        return buildingShape;
    }

    public void setBuildingShape(PShape buildingShape) {
        this.buildingShape = buildingShape;
    }
}
