package com.bioinspiredflight;

import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.gameobjects.ObjectiveObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.utilities.LevelHandler;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

/**
 * This is a placeholder class for Processing code.
 * The game is going to run from here.
 */
public class GameSketch extends PApplet{

    private GameActivity.GameSketchObserver obs;

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
    private PShape objectiveShape;
    private PShape innerLoopShape;
    private PShape outerLoopShape;
    private PShape helipadShape;
    private PShape collectionPointShape;
    private PShape collectibleShape;

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

    int currentLoopID;
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

    public void startLevel(String level){
        levelHandler.changeLevel(this, gameObjects, level);
        drone = gameObjects.getDrone();
        movingObject.setMovementSize(drone);
        PVector startPos = gameObjects.getDrone().coords;
        float temp = startPos.y;
        startPos.y = startPos.z;
        startPos.z = temp;
        movingObject.setPos(startPos);
        movingObject.setVel(new PVector(0,0,0));
        rotation = 0;
        drone.setInputToOutput(io);
        obs.updateUINewLevel();
        currentLoopID = 0;
    }

    public void setup() {
        frameRate(30);
        droneBodyShape = loadShape("textured_circular_drone_sans_propellers.obj");
        buildingShape = loadShape("textured_drone_sans_propellers.obj");
        outerLoopShape = loadShape("loop.obj");
        outerLoopShape.setFill(color( 255, 195, 0, 245));
        innerLoopShape = loadShape("textured_circular_drone.obj");
        innerLoopShape.setFill(color(  152, 226, 255, 90));
        helipadShape = loadShape("simple_helipad.obj");
        collectibleShape = loadShape("textured_drone_sans_propellers.obj");
        collectionPointShape = loadShape("simple_helipad.obj");
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
        startLevel("levels/level0.csv");
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
        if (getMovingObject().getAcc().z != -250) { drone.spinPropellers((getMovingObject().getAcc().z + 250) / 1000); }
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

        /*
        for (int b = 0; b < buildings.length && movingObject.collided == false ; b++) {
                movingObject.collisionDetectorZ(movingObject, buildings[b]);
                movingObject.collisionDetectorXY(movingObject, buildings[b]);
                movingObject.isCollision(movingObject, buildings[b]);
//                System.out.println("collided? " + movingObject.collided);
//                System.out.println("building position: " + buildings[b].coords);
//                System.out.println("drone position: " + movingObject.getPos());
        }*/
        int i = gameObjects.checkForCollisions(movingObject);
        //System.out.println(i);

        if(movingObject.collided == true){
//            System.out.println("CollideMod's Saved Position!!!: " + lastNonCollision);
            collideMod.accept(visitor, movingObject, this, gameObjects.get(i));
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

    public PShape getBuildingShape() {
        return buildingShape;
    }

    public PShape getObjectiveShape() {
        return objectiveShape;
    }

    public Boolean checkCompleted(){
        ArrayList<ObjectiveObject> gameObjectives = gameObjects.getObjectiveList();
        Boolean complete = true;
        for (ObjectiveObject g :gameObjectives){
            if (g.getStatus() == false) { complete = false; }
        }
        if(complete == true) {
            obs.updateUiComplete();
        }
        return complete;
    }

    public void setObs(GameActivity.GameSketchObserver obs) {
        this.obs = obs;
    }

    public PShape getOuterLoopShape() {
        return outerLoopShape;
    }

    public PShape getInnerLoopShape() {
        return innerLoopShape;
    }

    public PShape getHelipadShape() {
        return helipadShape;
    }

    public void setCurrentLoopID(int id) { currentLoopID = id; }

    public int getCurrentLoopID() { return currentLoopID; }

    public PShape getCollectionPointShape() {
        return collectionPointShape;
    }

    public PShape getCollectibleShape() {
        return collectibleShape;
    }

}
