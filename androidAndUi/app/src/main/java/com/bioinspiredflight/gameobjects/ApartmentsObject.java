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

public class ApartmentsObject extends GameObject {
    float rotation;

    public ApartmentsObject(GameSketch sketch, PShape body, float x, float y, float z,
                            float scale, float roty, int id) {
        super(sketch, body, x, y, z, id);
        h = 1320 * scale;
        w = 2810 * scale;
        d = 1290 * scale;
        rotation = roty;
        // Lower Body
        createHitbox(x, 295, z, 590, 2475, 1125);
        // Platform
        createHitbox(x, 445, z, 50, 2820, 1500);
        // Legs
        createHitbox(x + 1323, 210, z - 653, 420, 135, 135);
        createHitbox(x - 1323, 210, z - 653, 420, 135, 135);
        createHitbox(x + 1323, 210, z + 653, 420, 135, 135);
        createHitbox(x - 1323, 210, z + 653, 420, 135, 135);
        // Front & Back Balconies - Narrow
        createHitbox(x + 1105, 640, z, 130, 200, 1480);
        createHitbox(x + 1105, 938, z, 130, 200, 1480);
        createHitbox(x + 1105, 1619, z, 128, 200, 1480);
        createHitbox(x + 1105, 1260, z, 186, 200, 1480);
        createHitbox(x - 1105, 640, z, 130, 200, 1480);
        createHitbox(x - 1105, 938, z, 130, 200, 1480);
        createHitbox(x - 1105, 1619, z, 128, 200, 1480);
        createHitbox(x - 1105, 1260, z, 186, 200, 1480);
        // Front & Back Balconies - Wide
        createHitbox(x, 640, z, 130, 400, 1440);
        createHitbox(x, 938, z, 130, 400, 1440);
        createHitbox(x, 1619, z, 128, 400, 1440);
        createHitbox(x, 1260, z, 186, 400, 1480);
        createHitbox(x + 600, 640, z, 130, 400, 1480);
        createHitbox(x + 600, 938, z, 130, 400, 1480);
        createHitbox(x + 600, 1619, z, 128, 400, 1440);
        createHitbox(x + 600, 1260, z, 186, 400, 1480);
        createHitbox(x - 600, 640, z, 130, 400, 1480);
        createHitbox(x - 600, 938, z, 130, 400, 1480);
        createHitbox(x - 600, 1619, z, 128, 400, 1440);
        createHitbox(x - 600, 1260, z, 186, 400, 1480);
        // Side Balconies
        createHitbox(x, 640, z, 130, 2930, 840);
        createHitbox(x, 938, z, 130, 2930, 840);
        createHitbox(x, 1619, z, 128, 2980, 840);
        createHitbox(x, 1260, z, 186, 2980, 840);
    }

    private void createHitbox(float x, float y, float z, float h, float w, float d) {
        HitboxObject hb = new HitboxObject(this.sketch, this.body, x, y, z, this.id);
        hb.setHWD(h, w, d);
        sketch.gameObjects.add(hb);
    }
    //Test Instantiator
    public ApartmentsObject(float x, float y, float z, float scale, float rot, int id){
        super( x, y, z, id);
        h = 1320 * scale;
        w = 2810 * scale;
        d = 1290 * scale;

        rotation = rot;

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
        sketch.translate(getCoords().x, getCoords().y - getH()/2 - 580, getCoords().z);
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
