package com.bioinspiredflight.gameobjects;


import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
public abstract class GameObject implements Interactable{
    PShape body;
    public PVector coords;  //keeping this public for optimization reasons
    PApplet sketch;

    public GameObject(PApplet sketch, float x, float y, float z, String objectFileName){
        this.sketch = sketch;
        this.coords = new PVector(x, y, z);
        this.body = this.sketch.loadShape(objectFileName);
    }

}
