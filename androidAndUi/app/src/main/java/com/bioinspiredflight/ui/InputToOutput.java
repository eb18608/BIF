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

package com.bioinspiredflight.ui;

import com.bioinspiredflight.ui.Joystick;
import com.bioinspiredflight.ui.Slider;

import javax.vecmath.Vector3d;
import com.bioinspiredflight.physics.Movement;
import processing.core.PApplet;
import processing.core.PVector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class InputToOutput implements Joystick.JoystickListener, Slider.SliderListener {

    final private PVector vector3d;
    private float rotation;
    private float totalRotation;
    private UiSurfaceView view;
    private boolean usingJoystick, usingSlider;

    //Get individual component from PVector
    public float getX(PVector p){
        return p.x;
    }
    public float getY(PVector p){
        return p.y;
    }
    public float getZ(PVector p){
        return p.z;
    }

    //Set individual componend in PVector
    public void setX(PVector p, float x){
        p.x = x;
    }
    public void setY(PVector p, float y){
        p.y = y;
    }
    public void setZ(PVector p, float z){
        p.z = z;
    }
    public void setV(PVector p, float x, float y, float z){
        p.x = x;
        p.y = y;
        p.z = z;
    }

    public InputToOutput(){
        vector3d = new PVector(0.0f, 0.0f, 0.0f);
        setX(vector3d, 0);
        setY(vector3d, 0);
        setZ(vector3d, -1);
        rotation = 0f;
        totalRotation = 0f;
        usingJoystick = false;
        usingSlider = false;
    }

    public void setView(UiSurfaceView view) {
        this.view = view;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        setX(vector3d, xPercent);
        setY(vector3d, -yPercent);
        rotateVector();
    }

    @Override
    public void onSliderMoved(float zPercent, float rotation, int id) {
        setZ(vector3d, zPercent);
        this.rotation = rotation;
    }

    public boolean isUsingJoystick(){
        return usingJoystick;
    }

    public boolean isUsingSlider(){
        return usingSlider;
    }

    public void setUsingJoystick(boolean using){
        this.usingJoystick = using;
    }

    public void setUsingSlider(boolean using){
        this.usingSlider = using;
    }

    public PVector getVector(){
        return vector3d;
    }

    public float getRotation() {
        return rotation;
    }

    public void setTotalRotation(float totalRotation) {
        this.totalRotation = totalRotation;
    }

    private void rotateVector(){
        float x = getX(vector3d);
        float y = getY(vector3d);
        float newX = (float) ((cos(totalRotation) * x) - (sin(totalRotation) * y));
        float newY = (float) ((sin(totalRotation) * x) + (cos(totalRotation) * y));
        setX(vector3d, newX);
        setY(vector3d, newY);
    }

}
