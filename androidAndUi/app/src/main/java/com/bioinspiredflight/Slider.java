package com.bioinspiredflight;

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
    public float sliderRadius;
    public float centerX;
    public float centerY;
    public float sliderWidthFromCenter;
    public float sliderHeightFromCenter;
    private int left, top, right, bottom;
    public boolean usingSlider;

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

        sliderWidthFromCenter = w / 40f;
        sliderHeightFromCenter = h / 6.5f;
        left = (int) (centerX - sliderWidthFromCenter);
        top = (int) (centerY - sliderHeightFromCenter);
        right = (int) (centerX + sliderWidthFromCenter);
        bottom = (int) (centerY + sliderHeightFromCenter);
    }

    public void drawSlider(float x, float y){
        if (getHolder().getSurface().isValid()){
            System.out.println("...");
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors  = new Paint();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawRect(new Rect(left, top, right, bottom), colors);
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
        if (displacementX >= -sliderWidthFromCenter && displacementX <= sliderWidthFromCenter
                && displacementY >= -sliderHeightFromCenter && displacementY <= sliderHeightFromCenter) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("slide");
        //if (v.equals(this)) {
            float displacementX = (float) event.getX() - centerX;
            float displacementY = (float) event.getY() - centerY;
            if (displacementX >= -sliderWidthFromCenter && displacementX <= sliderWidthFromCenter
                    && displacementY >= -sliderHeightFromCenter && displacementY <= sliderHeightFromCenter) {
                usingSlider = true;
            }
            if (event.getAction() != MotionEvent.ACTION_UP && usingSlider) {
                //System.out.println("Touching slider");
                if (displacementY >= -sliderHeightFromCenter && displacementY <= sliderHeightFromCenter){
                    drawSlider(centerX, event.getY());
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

}
