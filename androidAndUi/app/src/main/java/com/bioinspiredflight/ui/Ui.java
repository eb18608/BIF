package com.bioinspiredflight.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.MainActivity;

import java.util.ArrayList;

import processing.android.CompatUtils;

public class Ui {

    private Button returnButton;
    private Joystick testJoystick;
    private Slider testSlider;
    private UiSurfaceView uiSurfaceView;
    private ArrayList<View> widgets;
    private InputToOutput io;
    private GameActivity.GameSketchObserver obs;
    //private final FrameLayout frame;
    //private final AppCompatActivity gameActivity;

    public Ui(final GameActivity gameActivity, InputToOutput io) {
        setupWidgets(gameActivity);
        //this.io = new InputToOutput();
        this.io = io;
        uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io);
        widgets.add(uiSurfaceView);
        this.io.setView(uiSurfaceView);

    }

    public Ui(final GameActivity gameActivity, InputToOutput io, boolean sliderToggle) {
        setupWidgets(gameActivity);
        //this.io = new InputToOutput();
        this.io = io;
        uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io, sliderToggle);
        widgets.add(uiSurfaceView);
        this.io.setView(uiSurfaceView);

    }

    private void setupWidgets(final GameActivity gameActivity){
        widgets = new ArrayList<>();
        returnButton = new Button(gameActivity);
        returnButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        returnButton.setText("Change Level");
        returnButton.setId(CompatUtils.getUniqueViewId());
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(gameActivity.getApplicationContext(), "Changing level",
                        Toast.LENGTH_SHORT)
                        .show();
                //gameActivity.finish();
                obs.updateGameSketch();

            }
        });
        widgets.add(returnButton);

        testJoystick = new Joystick(gameActivity);
        testJoystick.setId(CompatUtils.getUniqueViewId());
        testJoystick.addJoystickListener(new InputToOutput());
        widgets.add(testJoystick);
        testSlider = new Slider(gameActivity);
        testSlider.setId(CompatUtils.getUniqueViewId());
        widgets.add(testSlider);
    }

    public void drawUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.addView(widget);
            System.out.printf("Added %s\n", widget.toString());
        }
    }

    public void setObs(GameActivity.GameSketchObserver obs) {
        this.obs = obs;
    }

    /*
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }*/
}
