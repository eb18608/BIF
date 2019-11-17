package com.bioinspiredflight;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ui extends SurfaceView implements SurfaceHolder.Callback{

    private Button testButton;
    private Joystick testJoystick;
    private ArrayList<View> widgets;
    //private final FrameLayout frame;
    //private final AppCompatActivity gameActivity;

    public Ui(final Context context) {
        super(context);
        getHolder().addCallback(this);
        widgets = new ArrayList<>();
        testButton = new Button(context);
        testButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        testButton.setText("Test Button");
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "It works!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        widgets.add(testButton);
        testJoystick = new Joystick(context);
        testJoystick.setDimensions(0, 0, Math.min(getWidth(), getHeight()) / 3,
                Math.min(getWidth(), getHeight()) / 5);
        widgets.add(testJoystick);
    }

    public void drawUi(FrameLayout frame){
        for (View widget : widgets){
            frame.addView(widget);
        }
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
}
