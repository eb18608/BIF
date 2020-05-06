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

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class LoopObject extends ObjectiveObject {
    PShape innerBody;
    float innerBodyRotation;
    float outerBodyRotation;
    float[] colour = {255, 195, 0, 245};
    boolean visible;


    public LoopObject(GameSketch sketch, PShape outerBody, PShape innerBody, float x, float y, float z,
                          float scale, float rot, int id) {
        super(sketch, outerBody, x, y, z, scale, id);
        h = 20 * scale;
        w = 100 * scale;
        d = 100 * scale;
        this.innerBody = innerBody;
        outerBodyRotation = rot;
        if (this.getID() == 0) { visible = true; }
        else { visible = false; }
        this.setSolid(false);
    }

    public PVector getCoords(){
        return this.coords;
    }

    public float getH(){
        return h;
    }

    public float getW() { return w; }

    public float getD()  { return d; }

    @Override
    public boolean isVisible() { return visible; }

    void setColour(float r, float g, float b, float a) {
        colour[0] = r;
        colour[1] = g;
        colour[2] = b;
        colour[3] = a;
    }

    @Override
    public void draw3D() {
        if (sketch.getCurrentLoopID() == this.getID()) {
            visible = true;
        }
        if ( this.isVisible() ) {
            if (colour[1] == 0) {
                sketch.getObs().updateGameOver();
            }

            this.body.setFill(sketch.color(colour[0], colour[1], colour[2], colour[3]));

            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.pushMatrix();
            sketch.rotateY(outerBodyRotation);
            sketch.shape(body);
            sketch.popMatrix();
            sketch.pushMatrix();
            sketch.rotateY(innerBodyRotation);
            innerBodyRotation += 0.025f;
            sketch.shape(innerBody);
            sketch.popMatrix();
            sketch.popMatrix();

            if (!this.getStatus() && this.getID() != 0) {
                colour[1] -= 0.25f;
            }
        }
    }

    @Override
    public void draw2D() {
        if ((sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 3000) && this.isVisible() && SensorContent.ITEMS.get(4).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/20, -this.getCoords().z/20);
            sketch.fill(sketch.color(colour[0], colour[1], colour[2], 100));
            sketch.circle(0, 0, this.getW()/5);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        if (this.isVisible()) {
            if (!this.getStatus()) { sketch.setCurrentLoopID(this.getID() + 1); }
            setStatus(true);
            setColour(40, 255, 40, 245);
        }
    }

    @Override
    public boolean isDrone() {
        return false;
    }

    @Override
    public boolean shouldBeTracked() {
        if (!this.getStatus() && this.isVisible()) {
            return true;
        } else {
            return false;
        }
    }
}
