package com.bioinspiredflight;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class BuildingObject extends GameObject {
    private float h, w, d;

    public BuildingObject(PApplet sketch, float wid, float hei, float dep,
                          float x, float y, float z, String buildingFilename) {
        h = hei;
        w = wid;
        d = dep;
        coords = new PVector(x, y, z);
        this.sketch = sketch;
        body = this.sketch.loadShape(buildingFilename);
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

    public void draw() {
        sketch.shape(body);
    }
}
