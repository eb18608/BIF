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
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.core.PImage;

public class HelipadObject extends ObjectiveObject {
    PImage icon;

    public HelipadObject(GameSketch sketch, PShape body, float x, float y, float z,
                          float scale, int id) {
        super(sketch, body, x, y, z, scale, id);
        h = 6 * scale;
        w = 265 * scale;
        d = 265 * scale;
        icon = sketch.loadImage("HelipadIcon.png");
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

    @Override
    public void draw3D() {
        sketch.pushMatrix();
        sketch.translate(getCoords().x, getCoords().y, getCoords().z);
        sketch.shape(body);
        sketch.popMatrix();
    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 3000 && SensorContent.ITEMS.get(10).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/20 - this.getW()/20, -this.getCoords().z/20 - this.getD()/20);
            sketch.image(icon, 0, 0, this.getW()/10, this.getD()/10);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
        setStatus(true);
        if (!sketch.checkCompleted()) {
            setStatus(false);
        } else {
            sketch.getObs().updateUiComplete();
        }
    }

    @Override
    public boolean isDrone() { return false; }

    @Override
    public boolean shouldBeTracked() {
        setStatus(true);
        boolean allCompleted = sketch.checkCompleted();
        setStatus(false);
        return allCompleted;
    }
}
