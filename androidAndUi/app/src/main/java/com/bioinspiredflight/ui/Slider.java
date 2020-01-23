package com.bioinspiredflight.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Slider extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float length;
    private float sliderRadius;
    private float centerX;
    private float centerY;
    private float verticalSliderWidthFromCenter;
    private float verticalSliderHeightFromCenter;
    private float horizontalSliderWidthFromCenter, horizontalSliderHeightFromCenter;
    private int verticalLeft, verticalTop, verticalRight, verticalBottom;
    private int horizontalLeft, horizontalTop, horizontalRight, horizontalBottom;
    private boolean usingSlider;
    private SliderListener listener;

    public float getLength() {
        return length;
    }

    public float getSliderRadius() {
        return sliderRadius;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getVerticalSliderWidthFromCenter() {
        return verticalSliderWidthFromCenter;
    }

    public float getVerticalSliderHeightFromCenter() {
        return verticalSliderHeightFromCenter;
    }

    public float getHorizontalSliderWidthFromCenter() {
        return horizontalSliderWidthFromCenter;
    }

    public float getHorizontalSliderHeightFromCenter() {
        return horizontalSliderHeightFromCenter;
    }

    public boolean isUsingSlider() {
        return usingSlider;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setSliderRadius(float sliderRadius) {
        this.sliderRadius = sliderRadius;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setVerticalSliderWidthFromCenter(float verticalSliderWidthFromCenter) {
        this.verticalSliderWidthFromCenter = verticalSliderWidthFromCenter;
    }

    public void setVerticalSliderHeightFromCenter(float verticalSliderHeightFromCenter) {
        this.verticalSliderHeightFromCenter = verticalSliderHeightFromCenter;
    }

    public void setUsingSlider(boolean usingSlider) {
        this.usingSlider = usingSlider;
    }

    public Slider(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        //this.setZOrderMediaOverlay(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
        this.usingSlider = false;
    }

    private void setup(){
        float h = getHeight();
        float w = getWidth();
        length = h / 2f;
        sliderRadius = Math.min(w, h) / 15f;
        centerX = w / 7f;
        centerY = h - (h / 3.2f);

        verticalSliderWidthFromCenter = w / 40f;
        verticalSliderHeightFromCenter = h / 6.5f;
        verticalLeft = (int) (centerX - verticalSliderWidthFromCenter);
        verticalTop = (int) (centerY - verticalSliderHeightFromCenter);
        verticalRight = (int) (centerX + verticalSliderWidthFromCenter);
        verticalBottom = (int) (centerY + verticalSliderHeightFromCenter);

        horizontalSliderWidthFromCenter = w / 15f;
        horizontalSliderHeightFromCenter = h / 20f;
        horizontalLeft = (int) (centerX - horizontalSliderWidthFromCenter);
        horizontalTop = (int) (centerY - horizontalSliderHeightFromCenter);
        horizontalRight = (int) (centerX + horizontalSliderWidthFromCenter);
        horizontalBottom = (int) (centerY + horizontalSliderHeightFromCenter);
    }

    public void drawSlider(float x, float y){
        if (getHolder().getSurface().isValid()){
            //System.out.println("...");
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors  = new Paint();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawRect(new Rect(verticalLeft, verticalTop, verticalRight, verticalBottom), colors);
            canvas.drawRect(new Rect(
                    horizontalLeft,
                    (int) (y + horizontalSliderHeightFromCenter),
                    horizontalRight,
                    (int) (y - horizontalSliderHeightFromCenter)),
                    colors);
            //colors.setARGB(255, 50, 50, 50);
            colors.setARGB(255, 0, 0xdb, 0xff);
            canvas.drawCircle(x, y, sliderRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public boolean withinBounds(float x, float y){
        boolean result = false;
        float displacementX = x - centerX;
        float displacementY = y - centerY;
        if (displacementX >= -verticalSliderWidthFromCenter && displacementX <= verticalSliderWidthFromCenter
                && displacementY >= -verticalSliderHeightFromCenter && displacementY <= verticalSliderHeightFromCenter) {
            result = true;
        }
        return result;
    }

    public void addSliderListener(SliderListener listener){
        this.listener = listener;
    }


    // Note that this function is only called if Slider is used on its own
    // and NOT as a part of UiSurfaceView
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("slide");
        //if (v.equals(this)) {
            float displacementX = (float) event.getX() - centerX;
            float displacementY = (float) event.getY() - centerY;
            if (displacementX >= -horizontalSliderWidthFromCenter && displacementX <= horizontalSliderWidthFromCenter
                    && displacementY >= -verticalSliderHeightFromCenter && displacementY <= verticalSliderHeightFromCenter) {
                usingSlider = true;
            }
            if (event.getAction() != MotionEvent.ACTION_UP && usingSlider) {
                System.out.println("Touching slider");
                if (displacementY >= -verticalSliderHeightFromCenter
                        && displacementY <= verticalSliderHeightFromCenter
                        && displacementX >= -horizontalSliderWidthFromCenter
                        && displacementX <= horizontalSliderWidthFromCenter){
                    drawSlider(event.getX(), event.getY());
                    return true;
                }
            } else {
                usingSlider = false;
                drawSlider(centerX, centerY);
                System.out.println("Released slider");
            }
        //}
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setup();
        drawSlider(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public interface SliderListener{
        void onSliderMoved(float zPercent, int id);
    }

}
