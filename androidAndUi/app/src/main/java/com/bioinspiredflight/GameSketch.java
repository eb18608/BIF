/**The MIT License (MIT)
 *
 * Copyright © 2020 Bio-Inspired Flight Lab, University of Bristol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bioinspiredflight;

import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.gameobjects.HitboxObject;
import com.bioinspiredflight.gameobjects.ObjectiveObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.ModVisitor;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.utilities.LevelHandler;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

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
    public GameObjectList gameObjects = new GameObjectList();
    private ArrayList<GameObject> collidingObjects = new ArrayList<>();

    private PShape droneBodyShape;
    private PShape buildingShape;
    private PShape objectiveShape;
    private PShape innerLoopShape;
    private PShape outerLoopShape;
    private PShape helipadShape;
    private PShape collectionPointShape;
    private PShape collectibleShape;
    private PShape airVentShape;
    private PShape airStreamShape;
    private PShape fuelShape;
    private PShape searchlightShape;
    private PShape skyscraperShape;
    private PShape apartmentsShape;
    private PShape arrow;

    private ReentrantLock lock = new ReentrantLock();

    private PImage sky;
    private PImage floorImage;

    private boolean loaded = false;

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
    int collectiblesHeld;
    PImage texture;
    PImage droneIcon;
    DroneObject drone;
    float rotation;
    int flipFrameCounter;
    PVector flipAcc = new PVector(0,0,0);
    PVector emptyAcc = new PVector(0,0,0);
    ArrayList<PVector> prevAccs = new ArrayList<>();
    private boolean gamePaused;
    boolean setupCompleted;
    private int framesPassed;
    private boolean timerStarted = false;
    int maxFuel = 1275;
    int fuelLevel = maxFuel;
    PImage fuelIcon;
    HitboxObject floor;

    public boolean levelContainsFuel() {
        for (GameObject object : gameObjects.getList()) {
            if (object.isFuel()) { return true; }
        }
        return false;
    }

    public boolean levelContainsSearchlights() {
        for (GameObject object : gameObjects.getList()) {
            if (object.isSearchlight()) { return true; }
        }
        return false;
    }

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

    public void renderFloor(float wid, float dep, PImage texture) {
        noTint();
        beginShape(QUADS);
        texture(texture);
        //frontface
        vertex(0, 0, 0, 1, 1);
        vertex(0, 0, wid, 0, 1);
        vertex(0, dep, wid, 0, 0);
        vertex(0, dep, 0, 1, 0);
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
        setupCompleted = false;
        levelHandler.changeLevel(this, gameObjects, level);
        drone = gameObjects.getDrone();
        movingObject.setMovementSize(drone);
        PVector startPos = gameObjects.getDrone().coords;
        float temp = startPos.y;
        startPos.y = startPos.z;
        startPos.z = temp;
        movingObject.setPos(startPos);
        movingObject.setVel(new PVector(0,0,0));
        drone.setInputToOutput(io);
        obs.updateUINewLevel();
        currentLoopID = 0;
        collectiblesHeld = 0;
        framesPassed = 0;
        timerStarted = false;
        fuelLevel = maxFuel;
        gameObjects.add(floor);
        if (levelContainsSearchlights()) {
            sky = loadImage("nightsky.png");
        } else {
            sky = loadImage("sky.png");
        }
        sky.resize(width, height);
        resizeFloor(levelHandler.getFloorW(), levelHandler.getFloorD());
        floorImage = loadImage(levelHandler.getFloorImageFilepath());
        setupCompleted = true;
    }

    // Loop through the acc values for the last 10 frames.
    // Check if the slider has been moved from one side to the other.
    // Check that the slider was at it's edge.
    // Check that the slider is within a +/-10 boundary.
    public boolean droneShouldflip(ArrayList<PVector> prevAccs, PVector currAcc) {
        if (Math.random() <= 0.02) { // ~20% chance to occur.
            for (PVector prevAcc : prevAccs) {
                if ((prevAcc.x > 0 && currAcc.x < 0) || (prevAcc.x < 0 && currAcc.x > 0)) { // The x's have opposite signs
                    if ((prevAcc.y > 0 && currAcc.y < 0) || (prevAcc.y < 0 && currAcc.y > 0)) { // The y's have opposite signs
                        if (Math.abs(currAcc.x) + Math.abs(currAcc.y) > 300) { // currAcc at edge of joystick.
                            if (Math.abs(prevAcc.x) + Math.abs(prevAcc.y) > 300) { // prevAcc at edge of joystick.
                                if ((Math.abs(currAcc.x) < Math.abs(prevAcc.x) + 10) && (Math.abs(currAcc.x) > Math.abs(prevAcc.x) - 10)) { // x within +/- 10 boundary.
                                    if ((Math.abs(currAcc.y) < Math.abs(prevAcc.y) + 10) && (Math.abs(currAcc.y) > Math.abs(prevAcc.y) - 10)) { // y within +/- 10 boundary.
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private GameObject trackingObject() {
        GameObject closest = drone;
        float distance = 100000000;
        for (GameObject object : gameObjects.getObjectiveList()) {
            if (object.shouldBeTracked() && distanceToDrone(object) < distance) {
                distance = distanceToDrone(object);
                closest = object;
            }
        }
        return closest;
    }

    public void drawArrow() {
        pushMatrix();
        translate(0,12,0);
        rotateY(-drone.calculateAngle(trackingObject().coords.z - drone.coords.z, trackingObject().coords.x - drone.coords.x) + PI/2);
        shape(arrow);
        popMatrix();
    }

    public void setup() {
        if (gamePaused) { return; }
        frameRate(30);
        if (SensorContent.ITEMS.get(2).isEquipped()) {
            droneBodyShape = loadShape("camo_drone.obj");
        } else {
            droneBodyShape = loadShape("textured_circular_drone_sans_propellers.obj");
        }
        buildingShape = loadShape("cube.obj");
        skyscraperShape = loadShape("skyscraper.obj");
        apartmentsShape = loadShape("appartment_buildings.obj");
        outerLoopShape = loadShape("loop.obj");
        outerLoopShape.setFill(color( 255, 195, 0, 245));
        innerLoopShape = loadShape("textured_circular_drone.obj");
        innerLoopShape.setFill(color(  152, 226, 255, 90));
        helipadShape = loadShape("simple_helipad.obj");
        collectibleShape = loadShape("letter.obj");
        collectionPointShape = loadShape("postbox.obj");
        sky = loadImage("sky.png");
        sky.resize(width, height);
        arrow = loadShape("TurtleShell.obj");
        arrow.scale(1.2f);
        fuelIcon = loadImage("FuelIcon.png");
        fuelShape = loadShape("fuel.obj");
        airStreamShape = loadShape("airflow.obj");
        airVentShape = loadShape("textured_circular_drone.obj");
        floor = new HitboxObject(this, buildingShape, 0,-9.5f,0,0);
        floor.setSolid(false);
        floor.setCollisionsEnabled(false);
        textureMode(NORMAL);
        texture = loadImage("SkyscraperFront.png");
        droneIcon = loadImage("DroneIcon.png");
        for (int i = 0; i < 10; i++) {
            prevAccs.add(emptyAcc);
        }
        obs.startLevelSelect();
        setupCompleted = true;
    }

    public void draw3d(float droneLeftRight, float droneUpDown, float droneForwardBack){
        // 3D Section
        lights();
        background(sky);
        drone.move(droneLeftRight, droneUpDown, droneForwardBack);
        setCamera(scale);

        // Floor
        pushMatrix();
        noLights();
        translate(floor.getW()/2, 0, -floor.getD()/2);
        rotateZ(PI/2);
        renderFloor(floor.getW(), floor.getD(), floorImage);
        lights();
        popMatrix();

        gameObjects.drawNonDroneGameObjects3D();

        pushMatrix();
        rotation += io.getRotation() * rotationSpeed;
        io.setTotalRotation(-rotation);
        translate(drone.coords.x, drone.coords.y, drone.coords.z);
        if (SensorContent.ITEMS.get(9).isEquipped()) { drawArrow(); }
        if (droneShouldflip(prevAccs, movingObject.getAcc())) {
            flipFrameCounter = 20;
            flipAcc.set(movingObject.getAcc());
        }
        if (flipFrameCounter != 0) {
            drone.flipDrone(flipAcc);
            flipFrameCounter -= 1;
        } else {
            drone.tiltDrone(movingObject.getAcc());
        }
        rotateY(rotation);
        if (getMovingObject().getAcc().z != -300) { drone.spinPropellers((getMovingObject().getAcc().z + 300) / 1200); }
        drone.draw3D();
        rotateY(-rotation);
        popMatrix();

        if (!loaded){
            loaded = true;
        }
        prevAccs.remove(0);
        prevAccs.add(movingObject.getAcc());
    }

    public void draw2d(){
        // 2D Section
        hint(DISABLE_DEPTH_TEST);
        noLights();

        // Fuel
        if (levelContainsFuel()) {
            pushMatrix();
            translate(400, 60);
            for (int i = 255; i <= maxFuel; i += 255) {
                if (fuelLevel < i) {
                    noFill();
                    stroke(153, fuelLevel % 255);
                    rect(-5, -5, 100, 100);
                    tint(255, fuelLevel % 255);
                    image(fuelIcon, 0, 0, 90, 90);
                } else {
                    noFill();
                    stroke(153);
                    rect(-5, -5, 100, 100);
                    noTint();
                    image(fuelIcon, 0, 0, 90, 90);
                    translate(100, 0);
                }
            }
            noTint();
            popMatrix();
        }

        // Minimap
        if (SensorContent.ITEMS.get(5).isEquipped()) {
            pushMatrix();
            translate(width - 160, 160);

            fill(153);
            circle(0, 0, 300);

            pushMatrix();
            rotate(-rotation);
            pushMatrix();
            translate(-drone.coords.x / 20, drone.coords.z / 20);

            gameObjects.drawAllGameObjects2D();
            popMatrix();
            popMatrix();
            fill(0);
            image(droneIcon, -drone.di / 15, -drone.di / 15, drone.di / 7.5f, drone.di / 7.5f);
            popMatrix();
        }

        hint(ENABLE_DEPTH_TEST);
    }

    public void resizeSky() {
        sky.resize(width, height);
    }

    public void draw() {
        //if (gamePaused || !setupCompleted) { return; }
        if (!setupCompleted) { return; }
        gameObjects.checkForCollisions(movingObject, collidingObjects);

        for (GameObject object : collidingObjects){
            collideMod.accept(visitor, movingObject, this, object);
        }

        if(movingObject.collided == true){
            //collideMod.accept(visitor, movingObject, this, gameObjects.get(i));
            movingObject.collided = false;
        } else {
            setLastPosition(movingObject.getPos());
        }

        controlMod.accept(visitor, movingObject);

        float droneLeftRight = movingObject.getX(movingObject.getPos());
        float droneForwardBack = movingObject.getY(movingObject.getPos());
        float droneUpDown = movingObject.getZ(movingObject.getPos());

        image(floorImage, 0, 0);

        draw3d(droneLeftRight, droneUpDown, droneForwardBack);

        camera();
        draw2d();

        if (fuelLevel == 0) {
            obs.updateGameOver();
        } else if (levelContainsFuel()) {
            if (io.isUsingJoystick() || io.isUsingSlider()) {
                decrementFuelLevel(2);
            }
        }
        updateTimer();
    }

    private void updateTimer(){
        if (!timerStarted){
            long seconds = levelHandler.getTimeLimitSeconds();
            if (seconds > 0){
                obs.startTimer(seconds);
                timerStarted = true;
            }
        }
        if (timerStarted){
            framesPassed++;
            obs.updateUiTimer();
            framesPassed = 0;
        }
    }

    public void settings() {
        fullScreen(P3D);
    }

    public void resume() {
        gamePaused = false;
        this.loop();
        System.out.println("resuming here!");
    }

    public void pause() {
        gamePaused = true;
        this.noLoop();
        System.out.println("pausing here!");
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
        return complete;
    }

    public void setObs(GameActivity.GameSketchObserver obs) { this.obs = obs; }

    public GameActivity.GameSketchObserver getObs() { return this.obs; }

    public PShape getOuterLoopShape() { return outerLoopShape; }

    public PShape getInnerLoopShape() { return innerLoopShape; }

    public PShape getHelipadShape() { return helipadShape; }

    public void setCurrentLoopID(int id) { currentLoopID = id; }

    public int getCurrentLoopID() { return currentLoopID; }

    public PShape getCollectionPointShape() { return collectionPointShape; }

    public PShape getCollectibleShape() { return collectibleShape; }

    public int getCollectiblesHeld() { return collectiblesHeld; }

    public void setCollectiblesHeld(int amount) { collectiblesHeld = amount; }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isGamePaused(){
        return gamePaused;
    }
    public PShape getAirVentShape() { return airVentShape; }

    public PShape getAirStreamShape() { return airStreamShape; }

    public int getFuelLevel() { return fuelLevel; }

    public void incrementFuelLevel(int amount) {
        fuelLevel += amount;
        if (fuelLevel > maxFuel) { fuelLevel = maxFuel; }
    }

    public void decrementFuelLevel(int amount) {
        fuelLevel -= amount;
        if (fuelLevel < 0) { fuelLevel = 0; }
    }

    public PShape getFuelShape() { return fuelShape; }

    public PShape getSearchlightShape() { return searchlightShape; }

    public PShape getSkyscraperShape() { return skyscraperShape; }

    public PShape getApartmentsShape() { return apartmentsShape; }

    public PImage getFloorImage() { return floorImage; }

    public void setFloorImage(PImage image) { floorImage = image; }

    public void resizeFloor(int x, int z) { floor.setHWD(20, x, z); }

    public void setRotation(float rot) { rotation = rot; }
}
