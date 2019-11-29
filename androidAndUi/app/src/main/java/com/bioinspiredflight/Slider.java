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
    private float sliderRadius;
    private float centerX;
    private float centerY;
    private int left, top, right, bottom;

    public Slider(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.setZOrderOnTop(true);
        this.getHolder()
                .setFormat(PixelFormat.TRANSPARENT);
    }

    private void setup(){
        float h = getHeight();
        float w = getWidth();
        length = h / 2f;
        sliderRadius = Math.min(w, h) / 15f;
        centerX = w / 7f;
        centerY = h - (h / 3.2f);

        float sliderWidthFromCenter = w / 40f;
        float sliderHeightFromCenter = h / 6.5f;
        left = (int) (centerX - sliderWidthFromCenter);
        top = (int) (centerY - sliderHeightFromCenter);
        right = (int) (centerX + sliderWidthFromCenter);
        bottom = (int) (centerY + sliderHeightFromCenter);
    }

    private void drawSlider(float x, float y){
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
