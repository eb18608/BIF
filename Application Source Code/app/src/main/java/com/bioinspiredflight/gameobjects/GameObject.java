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

    //Constructor for tests (only need position and scales
    public GameObject(float x, float y, float z, int id){
        this.coords = new PVector( x, y, z);
        this.id = id;
        this.collisionsEnabled = true;
        this. isSolid = true;
    }

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
