package com.bioinspiredflight;

import processing.core.PShape;
import processing.core.PVector;

public class BuildingObject extends GameObject {
    private float h, w, d;
    private PVector coords;
    PShape body;

    public BuildingObject(float wid, float hei, float dep, float x, float y, float z, String buildingFilename) {
        h = hei;
        w = wid;
        d = dep;
        coords = new PVector(x, y, z);
        body = loadShape(buildingFilename);
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
        shape(body);
    }
}
