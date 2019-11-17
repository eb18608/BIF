package com.bioinspiredflight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback{

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;

    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public void setDimensions(float centerX, float centerY,
                                   float baseRadius, float hatRadius){
        setCenterX(centerX);
        setCenterY(centerY);
        setBaseRadius(baseRadius);
        setHatRadius(hatRadius);
    }

    public void setCenterX(float centerX){
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

    private void setup(){
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    private void drawJoystick(float x, float y){
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
        //setup();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
