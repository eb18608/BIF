package com.bioinspiredflight;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import processing.android.CompatUtils;

public class Ui extends SurfaceView implements SurfaceHolder.Callback{

    private Button testButton;
    private Joystick testJoystick;
    private Slider testSlider;
    private UiSurfaceView uiSurfaceView;
    private ArrayList<View> widgets;
    private InputToOutput io;
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
        testButton.setId(CompatUtils.getUniqueViewId());
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "It works!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        //widgets.add(testButton);

        testJoystick = new Joystick(context);
        testJoystick.setId(CompatUtils.getUniqueViewId());
        testJoystick.addJoystickListener(new InputToOutput());
        widgets.add(testJoystick);
        testSlider = new Slider(context);
        testSlider.setId(CompatUtils.getUniqueViewId());
        widgets.add(testSlider);
        this.io = new InputToOutput();
        uiSurfaceView = new UiSurfaceView(context, testJoystick, testSlider, io);
        widgets.add(uiSurfaceView);

    }

    public void drawUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.addView(widget);
            System.out.printf("Added %s\n", widget.toString());
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