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

import java.io.SyncFailedException;

import processing.core.PShape;

public class ObjectiveObject extends GameObject {
    boolean status = false;

    public ObjectiveObject(GameSketch sketch, PShape body, float x, float y, float z,
                          float scale, int id) {
        super(sketch, body, x, y, z, id);
        h = 600 * scale;
        w = 400 * scale;
        d = 400 * scale;
    }

    public Boolean getStatus(){
        return this.status;
    }
    public void setStatus(Boolean complete){
        this.status = complete;
    }
    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        setStatus(true);
    }

    @Override
    public boolean isDrone() {
        return false;
    }

    @Override
    public void draw3D() { }

    @Override
    public void draw2D() { }

}
