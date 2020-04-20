package com.bioinspiredflight.gameobjects;


import com.bioinspiredflight.GameSketch;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
public abstract class GameObject implements Interactable{
    PShape body;
    public PVector coords;  //keeping this public for optimization reasons
    GameSketch sketch;
    float h, w, d;
    int id;
    private boolean collisionsEnabled;
    private boolean isSolid;

    public GameObject(GameSketch sketch, PShape body, float x, float y, float z, int id){
        this.sketch = sketch;
        this.body = body;
        this.coords = new PVector(x, y, z);
        this.id = id;
        this.collisionsEnabled = true;
        this.isSolid = true;
    }

    public PVector getCoords(){
        return this.coords;
    }

    public float getH(){
        return h;
    }

    public float getW() {
        return w;
    }

    public float getD() {
        return d;
    }

    public int getID() { return id; }

    @Override
    public boolean shouldBeTracked() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public void setCollisionsEnabled(boolean collisionsEnabled) {
        this.collisionsEnabled = collisionsEnabled;
    }

    public boolean isCollisionsEnabled(){
        return collisionsEnabled;
    }
    public boolean isFuel() { return false; }

    public boolean isSearchlight() { return false; }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }
}
