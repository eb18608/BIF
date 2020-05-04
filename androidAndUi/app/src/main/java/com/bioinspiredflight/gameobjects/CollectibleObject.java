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
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class CollectibleObject extends ObjectiveObject {
    boolean visible;
    float rotation;

    public CollectibleObject(GameSketch sketch, PShape body, float x, float y, float z, float scale, float roty, int id) {
        super(sketch, body, x, y, z, scale, id);
        h = 6 * scale;
        w = 50 * scale;
        d = 25 * scale;
        visible = true;
        this.setSolid(false);
        rotation = roty;
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
    public boolean isVisible() { return visible; }

    @Override
    public void draw3D() {
        if ( this.isVisible() ) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.rotateY(rotation);
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() {
        if ((sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 3000) && this.isVisible() && SensorContent.ITEMS.get(4).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/20, -this.getCoords().z/20);
            sketch.fill(sketch.color(0, 200, 200, 100));
            sketch.circle(0, 0, this.getW()/5);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        if (!(!SensorContent.ITEMS.get(6).isEquipped() && sketch.getCollectiblesHeld() == 1)) {
            if (this.isVisible()) {
                setStatus(true);
                sketch.setCollectiblesHeld(sketch.getCollectiblesHeld() + 1);
            }
            visible = false;

        }
    }

    @Override
    public boolean isDrone() { return false; }

    @Override
    public boolean shouldBeTracked() {
        if (!SensorContent.ITEMS.get(6).isEquipped() && sketch.getCollectiblesHeld() == 1) {
            return false;
        } else {
            return isVisible();
        }
    }
}
