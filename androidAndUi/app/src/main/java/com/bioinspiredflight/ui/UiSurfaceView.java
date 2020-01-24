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
    private boolean sliderToggle;

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
        this.sliderToggle = true;
    }

    public UiSurfaceView(Context context, Joystick joystick, Slider slider, InputToOutput io, boolean sliderToggle) {
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
        this.sliderToggle = sliderToggle;
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
            //...
            if (event.getPointerCount() > 1){
                //System.out.println("aituy");
                int pointerId0 = event.getPointerId(0);
                int pointerId1 = event.getPointerId(1);
                MotionEvent.PointerCoords coords1 = new MotionEvent.PointerCoords();
                MotionEvent.PointerCoords coords2 = new MotionEvent.PointerCoords();
                event.getPointerCoords(event.findPointerIndex(pointerId0), coords1);
                //System.out.printf("%.3f, %.3f\n", coords1.x, coords1.y);
                event.getPointerCoords(event.findPointerIndex(pointerId1), coords2);
                //System.out.printf("%.3f, %.3f\n", coords2.x, coords2.y);
                updateUi((int) coords1.x, (int) coords1.y, v, event);
                updateUi((int) coords2.x, (int) coords2.y, v, event);
                if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP
                        || event.getActionMasked() == MotionEvent.ACTION_UP){
                    System.out.println("...");
                    if (pointerId0 == event.getPointerId(event.getActionIndex())){
                        resetJoystick(coords1.x, coords1.y, false);
                        if (sliderToggle){
                            resetSlider(coords1.x, coords1.y, false);
                        }
                    } else if (pointerId1 == event.getPointerId(event.getActionIndex())){
                        resetJoystick(coords2.x, coords2.y, false);
                        if (sliderToggle){
                            resetSlider(coords2.x, coords2.y, false);
                        }
                    }
                }
            } else {
                //System.out.println("...");
                //moveJoystick(event.getX(), event.getY());
                updateUi((int) event.getX(), (int) event.getY(), v, event);
                if (event.getAction() == MotionEvent.ACTION_UP){
                    resetJoystick(event.getX(), event.getY(), true);
                    if (sliderToggle){
                        resetSlider(event.getX(), event.getY(), true);
                    }
                }
            }
        }
        return true;
    }

    // Only call this on ACTION_POINTER_UP or ACTION_UP
    private void resetJoystick(float x, float y, boolean ignoreConstraints){
        System.out.println("reset");
        if (x > getWidth() / 2 || ignoreConstraints){
            joystick.drawJoystick(joystick.getCenterX(), joystick.getCenterY());
            joystick.setUsingJoystick(false);
            if (io != null){
                io.onJoystickMoved(0, 0, getId());
            }
        }
    }

    // Only call this on ACTION_POINTER_UP or ACTION_UP
    private void resetSlider(float x, float y, boolean ignoreConstraints){
        if (x <= getWidth() / 2 || ignoreConstraints){
            slider.drawSlider(slider.getCenterX(), slider.getCenterY());
            slider.setUsingSlider(false);
            if (io != null){
                io.onSliderMoved(0, 0, getId());
            }
        }
    }

    private void updateUi(int x, int y, View v, MotionEvent event){
        if (x > getWidth() / 2){
            //joystick.onTouch(v, event);
            moveJoystick(x, y);
        } else {
            //slider.onTouch(v, event);
            moveSlider(x, y);
        }
    }

    private void moveJoystick(float x, float y){
        float centerX = joystick.getCenterX();
        float centerY = joystick.getCenterY();
        float baseRadius = joystick.getBaseRadius();
        if (joystick.withinBounds(x, y)){
            joystick.setUsingJoystick(true);
        }
        //Log.i("Screen", "Responding...");
        if (joystick.isUsingJoystick()){
            if (joystick.withinBounds(x, y)){
                joystick.drawJoystick(x, y);

                if (io != null){
                    io.onJoystickMoved(
                            (x - centerX) / baseRadius,
                            (y - centerY) / baseRadius,
                            getId()
                    );
                }
                //Log.i("Joystick", "Touched!");
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
                //Log.i("Joystick", "Out of bounds!");
            }

        } else {
            Log.i("Joystick", "Released!");
            joystick.setUsingJoystick(false);
            joystick.setJoystickOutOfPlace(true);
        }

    }

    private void moveSlider(float x, float y){
        if (slider.withinBounds(x, y)) {
            slider.setUsingSlider(true);
        }
        float centerX = slider.getCenterX();
        float centerY = slider.getCenterY();
        float verticalHeightFromCenter = slider.getVerticalSliderHeightFromCenter();
        float horizontalWidthFromCenter = slider.getHorizontalSliderWidthFromCenter();
        //float displacementX = x - centerX;
        float displacementY = centerY - y;
        float displacementX = centerX - x;
        //
        if (slider.isUsingSlider()) {
            //System.out.println("Touching slider");
            if (displacementY >= -verticalHeightFromCenter
                    && displacementY <= verticalHeightFromCenter
                    && displacementX >= -horizontalWidthFromCenter
                    && displacementX <= horizontalWidthFromCenter){
                slider.drawSlider(x, y);
                //return true;
                //System.out.printf("X: %.3f, Y: %.3f\n", displacementX, displacementY);
                if (io != null){
                    io.onSliderMoved(
                            displacementY / verticalHeightFromCenter,
                            displacementX / horizontalWidthFromCenter,
                            getId()
                    );
                }
            } else {
                float constrainedY;
                float constrainedX;
                if (displacementY < -verticalHeightFromCenter){
                    constrainedY = centerY + verticalHeightFromCenter;
                } else if (displacementY > verticalHeightFromCenter){
                    constrainedY = centerY - verticalHeightFromCenter;
                } else {
                    constrainedY = y;
                }
                if (displacementX < -horizontalWidthFromCenter){
                    constrainedX = centerX + horizontalWidthFromCenter;
                } else if (displacementX > horizontalWidthFromCenter){
                    constrainedX = centerX - horizontalWidthFromCenter;
                } else {
                    constrainedX = x;
                    //System.out.println("lol");
                }
                slider.drawSlider(constrainedX, constrainedY);
                //System.out.printf("X: %.3f, Y: %.3f\n", constrainedX, constrainedY);
                //System.out.printf("CenterX: %.3f, Width: %.3f\n", centerX, horizontalWidthFromCenter);
                if (io != null){
                    io.onSliderMoved(
                            (centerY - constrainedY) / verticalHeightFromCenter,
                            (centerX - constrainedX) / horizontalWidthFromCenter,
                            getId()
                    );
                }
            }
        } else {
            slider.setUsingSlider(false);
            slider.drawSlider(centerX, centerY);
            //System.out.println("Released slider");
        }
    }
}
