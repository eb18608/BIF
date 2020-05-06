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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener listener;
    private boolean usingJoystick;
    private boolean joystickOutOfPlace;

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getBaseRadius() {
        return baseRadius;
    }

    public float getHatRadius(){
        return hatRadius;
    }

    public boolean isUsingJoystick() {
        return usingJoystick;
    }

    public boolean isJoystickOutOfPlace() {
        return joystickOutOfPlace;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setBaseRadius(float baseRadius) {
        this.baseRadius = baseRadius;
    }

    public void setHatRadius(float hatRadius) {
        this.hatRadius = hatRadius;
    }

    public void setUsingJoystick(boolean usingJoystick) {
        this.usingJoystick = usingJoystick;
    }

    public void setJoystickOutOfPlace(boolean joystickOutOfPlace) {
        this.joystickOutOfPlace = joystickOutOfPlace;
    }

    public Joystick(Context context) {
        super(context);
        usingJoystick = false;
        joystickOutOfPlace = false;
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
    }
    public Joystick(Context context, AttributeSet attributes) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
    }
    public Joystick(Context context, AttributeSet attributes, int style) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
    }

    private void setup(){
        int h = getHeight();
        int w = getWidth();
        baseRadius = Math.min(w, h) / 6.5f;
        hatRadius = Math.min(w, h) / 9f;
        centerX = w - (baseRadius * 2f);
        centerY = h - (baseRadius * 2f);
    }

    public void addJoystickListener(JoystickListener listener){
        this.listener = listener;
    }

    public void drawJoystick(float x, float y){
        if (getHolder().getSurface().isValid()){
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors  = new Paint();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 0, 0xdb, 0xff);
            canvas.drawCircle(x, y, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setup();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean withinBounds(float x, float y){
        boolean result = false;
        float displacement = (float)
                Math.sqrt(Math.pow(x - centerX, 2)
                        + Math.pow(y - centerY, 2));
        if (displacement < baseRadius){
            result = true;
        }
        return result;
    }

    public void updateJoystick(float x, float y){
        drawJoystick(x, y);
        if (listener != null){
            listener.onJoystickMoved(
                    (x - centerX) / baseRadius,
                    (y - centerY) / baseRadius,
                    getId()
            );
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
            float displacement = (float)
                    Math.sqrt(Math.pow(event.getX() - centerX, 2)
                            + Math.pow(event.getY() - centerY, 2));
            if (displacement < baseRadius){
                usingJoystick = true;
            }
            if (event.getAction() != MotionEvent.ACTION_UP && usingJoystick){
                if (displacement < baseRadius){
                    updateJoystick(event.getX(), event.getY());
                } else {
                    float ratio = baseRadius/displacement;
                    float constrainedX =
                            centerX + (event.getX() - centerX) * ratio;
                    float constrainedY =
                            centerY + (event.getY() - centerY) * ratio;
                    updateJoystick(constrainedX, constrainedY);
                }
                Log.i("Joystick", "Touched!");
            } else {
                usingJoystick = false;
                joystickOutOfPlace = true;
            }
            if (joystickOutOfPlace){
                drawJoystick(centerX, centerY);
                joystickOutOfPlace = false;
            }
        return true;
    }

    public interface JoystickListener{
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
