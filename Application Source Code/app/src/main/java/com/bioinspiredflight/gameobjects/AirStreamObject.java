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

public class AirStreamObject extends GameObject {
    PVector direction = new PVector();
    float strength = 1.5f;
    float rotation = 0.1f;
    public AirStreamObject(GameSketch sketch, PShape body, float x, float y, float z,
                           float scale, float dirY, int id, float dirX, float dirZ) {
        super(sketch, body, x, y, z, id);
        h = 4000 * scale;
        w = 200 * scale;
        d = 200 * scale;
        direction.y = dirY;
        direction.x = dirX;
        direction.z = dirZ;
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
        if (SensorContent.ITEMS.get(0).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.rotateY(rotation);
            sketch.shape(body);
            rotation += 0.1f;
            sketch.popMatrix();
        }

        /*Possible rotations:   (1, id, 0, 0) .csv -> ( 0, 0, 1) .(Pvector) direction
                                    points up -> No rotation
        *                       (0, id, 1, 0) .csv -> ( 1, 0, 0) .(PVector) direction
                                    points right -> 90* z axis
        *                       (0, id,-1, 0) .csv -> (-1, 0, 0) .(PVector) direction
                                    points left -> -90* z axis
        *                       (0, id, 0, 1) .csv -> ( 0, 0, 1) .(PVector) direction
                                    points forward -> 90* x axis*/
    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        // These work just like controlMod,
        // Values need to be tweaked

        /* File reader reads ( .... yValue, id, xValue, zValue)
           Put into (PVector)direction = (xValue, zValue, yValue) -> Old physics coordinate scheme
           Run through physics engine like a control input basically
                                                                */
        PVector streamVector = new PVector(direction.x*strength,direction.z*strength , direction.y*strength);
        movement.setInStream(true);
        PVector resultantAcc = movement.forceApplied(
                movement.getAcc(),
                streamVector,
                movement.getMass(),
                movement.frametime);
        PVector resultantVel = movement.calcVel(
                movement.getVel(),
                resultantAcc,
                movement.frametime);
        PVector resultantPos = movement.calcPos(
                movement.getPos(),
                resultantVel,
                movement.frametime);
        movement.updateMover(resultantAcc, resultantVel, resultantPos, movement);
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}

