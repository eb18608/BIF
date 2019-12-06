package com.bioinspiredflight.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class UiSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private Joystick joystick;
    private Slider slider;
    private InputToOutput io;

    public UiSurfaceView(Context context, Joystick joystick, Slider slider, InputToOutput io) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
        this.joystick = joystick;
        this.slider = slider;
        this.io = io;
        this.joystick.addJoystickListener(io);
        this.slider.addSliderListener(io);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.equals(this)){

            if (event.getPointerCount() > 1){
                //System.out.println("aituy");
                int pointerId0 = event.getPointerId(0);
                int pointerId1 = event.getPointerId(1);
                MotionEvent.PointerCoords coords1 = new MotionEvent.PointerCoords();
                MotionEvent.PointerCoords coords2 = new MotionEvent.PointerCoords();
                event.getPointerCoords(event.findPointerIndex(pointerId0), coords1);
                System.out.printf("%.3f, %.3f\n", coords1.x, coords1.y);
                event.getPointerCoords(event.findPointerIndex(pointerId1), coords2);
                System.out.printf("%.3f, %.3f\n", coords2.x, coords2.y);
                updateUi((int) coords1.x, (int) coords1.y, v, event);
                updateUi((int) coords2.x, (int) coords2.y, v, event);
            } else {
                //System.out.println("...");
                //moveJoystick(event.getX(), event.getY());
                updateUi((int) event.getX(), (int) event.getY(), v, event);
            }
            if (!joystick.usingJoystick){
                joystick.drawJoystick(joystick.centerX, joystick.centerY);
                joystick.joystickOutOfPlace = false;
            }
        }
        return true;
    }

    private void updateUi(int x, int y, View v, MotionEvent event){
        /*
        if (joystick.withinBounds(x, y)){
            //joystick.onTouch(v, event);
            moveJoystick(x, y);
        } else if (slider.withinBounds(x, y)){
            //slider.onTouch(v, event);
            moveSlider(x, y);
        }*/
        if (x > getWidth() / 2){
            //joystick.onTouch(v, event);
            moveJoystick(x, y);
        } else {
            //slider.onTouch(v, event);
            moveSlider(x, y);
        }
    }

    private void moveJoystick(float x, float y){
        float centerX = joystick.centerX;
        float centerY = joystick.centerY;
        float baseRadius = joystick.baseRadius;
        if (joystick.withinBounds(x, y)){
            joystick.usingJoystick = true;
        }
        //Log.i("Screen", "Responding...");
        if (joystick.usingJoystick){
            if (joystick.withinBounds(x, y)){
                joystick.drawJoystick(x, y);

                if (io != null){
                    io.onJoystickMoved(
                            (x - centerX) / baseRadius,
                            (y - centerY) / baseRadius,
                            getId()
                    );
                }
                Log.i("Joystick", "Touched!");
            } else {
                float displacement = (float)
                        Math.sqrt(Math.pow(x - centerX, 2)
                                + Math.pow(y - centerY, 2));
                float ratio = baseRadius/displacement;
                float constrainedX =
                        centerX + (x - centerX) * ratio;
                float constrainedY =
                        centerY + (y - centerY) * ratio;
                joystick.drawJoystick(constrainedX, constrainedY);

                if (io != null){
                    io.onJoystickMoved(
                            (constrainedX - centerX) / baseRadius,
                            (constrainedY - centerY) / baseRadius,
                            getId()
                    );
                }
                Log.i("Joystick", "Out of bounds!");
            }

        } else {
            Log.i("Joystick", "Released!");
            joystick.usingJoystick = false;
            joystick.joystickOutOfPlace = true;
        }

    }

    private void moveSlider(float x, float y){
        if (slider.withinBounds(x, y)) {
            slider.usingSlider = true;
        }
        float centerX = slider.centerX;
        float centerY = slider.centerY;
        //float displacementX = x - centerX;
        float displacementY = centerY - y;
        if (slider.usingSlider) {
            //System.out.println("Touching slider");
            if (displacementY >= -slider.sliderHeightFromCenter && displacementY <= slider.sliderHeightFromCenter){
                slider.drawSlider(centerX, y);
                //return true;
                if (io != null){
                    io.onSliderMoved(
                            displacementY,
                            getId()
                    );
                }
            } else {
                float constrainedY;
                if (displacementY < -slider.sliderHeightFromCenter){
                    constrainedY = -slider.sliderHeightFromCenter;
                } else {
                    constrainedY = slider.sliderHeightFromCenter;
                }
                if (io != null){
                    io.onSliderMoved(
                            constrainedY,
                            getId()
                    );
                }
            }
        } else {
            slider.usingSlider = false;
            slider.drawSlider(centerX, centerY);
            System.out.println("Released slider");
        }
    }
}
