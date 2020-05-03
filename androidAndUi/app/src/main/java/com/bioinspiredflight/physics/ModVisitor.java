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

package com.bioinspiredflight.physics;

import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.GameSketch;

import processing.core.PVector;

public class ModVisitor implements Visitor{
    @Override
    public void visit(ControlMod control, Movement movement) {
        PVector resultantAcc = movement.forceApplied(
                movement.getAcc(),
                control.controlMod,
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
    public void visit(EnviroMod enviro, Movement movement) {

    }

    @Override
    public void visit(CollideMod collide, Movement movement) {
        movement.setVel(collide.collideMod);
    }

    @Override
    public void visit(ControlMod control, Movement movement, GameSketch sketch) {

    }

    @Override
    public void visit(CollideMod collide, Movement movement, GameSketch sketch) {
        movement.setVel(collide.collideMod);
        movement.setPos(sketch.getLastPosition());
    }

    @Override
    public void visit(CollideMod collide, Movement movement, GameSketch sketch, GameObject gameObject) {
        if (gameObject.isCollisionsEnabled()){
            gameObject.collide(collide, movement, sketch);
        }
    }

    @Override
    public void visit(EnviroMod enviro) {
    }

    @Override
    public void visit(ControlMod control) {
    }

    @Override
    public void visit(CollideMod collide) {
    }

}
