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

import processing.core.PShape;
import processing.core.PVector;

public class SkyscraperObject extends GameObject {
    float rotation;
    HitboxObject hb1, hb2, hb3, hb4;

    public SkyscraperObject(GameSketch sketch, PShape body, float x, float y, float z,
                            float scale, float rot, int id) {
        super(sketch, body, x, y, z, id);
        h = 4240 * scale;
        w = 1420 * scale;
        d = 1420 * scale;
        rotation = rot;

        hb1 = new HitboxObject(sketch, body, x, h, z, id);
        hb1.setHWD(244, 1056, 1076);
        sketch.gameObjects.add(hb1);
        hb2 = new HitboxObject(sketch, body, x, h + 226, z, id);
        hb2.setHWD(226, 712, 732);
        sketch.gameObjects.add(hb2);
        hb3 = new HitboxObject(sketch, body, x, h + 446, z, id);
        hb3.setHWD(220, 110, 110);
        sketch.gameObjects.add(hb3);
        hb4 = new HitboxObject(sketch, body, x, h + 768, z, id);
        hb4.setHWD(600, 34, 34);
        sketch.gameObjects.add(hb4);
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
        sketch.translate(getCoords().x, getCoords().y - getH()/2, getCoords().z);
        sketch.rotateY(rotation);
        sketch.shape(body);
        sketch.popMatrix();
    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 3000) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/20 - this.getW()/40, -this.getCoords().z/20 - this.getD()/40);
            sketch.fill(200);
            sketch.rect(0, 0, this.getW()/20, this.getD()/20);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        movement.setVel(collideMod.collideMod);
        movement.setPos(sketch.getLastPosition());
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
