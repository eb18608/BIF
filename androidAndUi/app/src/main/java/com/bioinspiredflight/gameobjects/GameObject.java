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
    public GameObject(GameSketch sketch, PShape body, float x, float y, float z, int id){
        this.sketch = sketch;
        this.body = body;
        this.coords = new PVector(x, y, z);
        this.id = id;
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

}
