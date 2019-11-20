package com.bioinspiredflight;

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

    public Joystick(Context context) {
        super(context);
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
    }
    public Joystick(Context context, AttributeSet attributes, int style) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    private void setup(){
        baseRadius = Math.min(getWidth(), getHeight()) / 5;
        hatRadius = Math.min(getWidth(), getHeight()) / 7;
        centerX = getWidth() - baseRadius;
        centerY = getHeight() - baseRadius;
    }

    public void addJoystickListener(JoystickListener listener){
        this.listener = listener;
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
        setup();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //return false;
        if (v.equals(this)){
            //Log.i("Screen", "Responding...");
            if (event.getAction() != MotionEvent.ACTION_UP){
                float displacement = (float)
                        Math.sqrt(Math.pow(event.getX() - centerX, 2)
                        + Math.pow(event.getY() - centerY, 2));
                if (displacement < baseRadius){
                    drawJoystick(event.getX(), event.getY());
                    if (listener != null){
                        listener.onJoystickMoved(
                                (event.getX() - centerX) / baseRadius,
                                (event.getY() - centerY) / baseRadius,
                                getId()
                        );
                    }
                } else {
                    float ratio = baseRadius/displacement;
                    float constrainedX =
                            centerX + (event.getX() - centerX) * ratio;
                    float constrainedY =
                            centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    if (listener != null){
                        listener.onJoystickMoved(
                                (constrainedX - centerX) / baseRadius,
                                (constrainedY - centerY) / baseRadius,
                                getId()
                        );
                    }
                }
                Log.i("Joystick", "Touched!");
            } else {
                drawJoystick(centerX, centerY);
                //Log.i("Joystick", "Released!");
            }
        }
        return true;
    }

    public interface JoystickListener{
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
