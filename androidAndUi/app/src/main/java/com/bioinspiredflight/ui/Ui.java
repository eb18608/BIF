package com.bioinspiredflight.ui;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import processing.android.CompatUtils;

public class Ui {

    private Button returnButton;
    private Button newLevelButton;
    private final TableLayout layout;
    private Joystick testJoystick;
    private Slider testSlider;
    private UiSurfaceView uiSurfaceView;
    private ArrayList<View> widgets;
    private InputToOutput io;
    private GameActivity.GameSketchObserver obs;
    //private final FrameLayout frame;
    //private final AppCompatActivity gameActivity;

    public Ui(final GameActivity gameActivity, InputToOutput io) {
        layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity);
        this.io = io;
        uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io);
        widgets.add(uiSurfaceView);
        this.io.setView(uiSurfaceView);

    }

    public Ui(final GameActivity gameActivity, InputToOutput io, boolean sliderToggle) {
        layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity);
        this.io = io;
        uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io, sliderToggle);
        widgets.add(uiSurfaceView);
        this.io.setView(uiSurfaceView);

    }

    private void setupWidgets(final GameActivity gameActivity){
        DisplayMetrics metrics = new DisplayMetrics();
        gameActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widgets = new ArrayList<>();
        newLevelButton = new Button(gameActivity);
        RelativeLayout.LayoutParams newLevelLayoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        final int sideMargin = metrics.widthPixels / 4;
        final int topMargin = 3 * (metrics.heightPixels / 10);
        final int bottomMargin = 3 * (metrics.heightPixels / 10);
        final int buttonThickness = metrics.heightPixels / 5;
        newLevelLayoutParams.leftMargin = sideMargin;
        newLevelLayoutParams.rightMargin = sideMargin;
        newLevelLayoutParams.topMargin = topMargin;
        newLevelLayoutParams.height = buttonThickness;

        newLevelButton.setLayoutParams(newLevelLayoutParams);
        newLevelButton.setText("Change Level");
        newLevelButton.setId(CompatUtils.getUniqueViewId());
        newLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(gameActivity.getApplicationContext(), "Changing level",
                        Toast.LENGTH_SHORT)
                        .show();
                //gameActivity.finish();
                obs.updateGameSketch();

            }
        });

        returnButton = new Button(gameActivity);
        RelativeLayout.LayoutParams returnLayoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        returnLayoutParams.leftMargin = sideMargin;
        returnLayoutParams.rightMargin = sideMargin;
        returnLayoutParams.topMargin = topMargin + buttonThickness;
        returnLayoutParams.height = buttonThickness;
        returnButton.setLayoutParams(returnLayoutParams);
        returnButton.setText("Exit");
        returnButton.setId(CompatUtils.getUniqueViewId());
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(gameActivity.getApplicationContext(), "Exiting",
                        Toast.LENGTH_SHORT)
                        .show();
                gameActivity.finish();
                //obs.updateGameSketch();

            }
        });


        //widgets.add(newLevelButton);
        //widgets.add(returnButton);


        testJoystick = new Joystick(gameActivity);
        testJoystick.setId(CompatUtils.getUniqueViewId());
        testJoystick.addJoystickListener(new InputToOutput());
        widgets.add(testJoystick);
        testSlider = new Slider(gameActivity);
        testSlider.setId(CompatUtils.getUniqueViewId());
        widgets.add(testSlider);
        widgets.add(newLevelButton);
        widgets.add(returnButton);
        returnButton.setVisibility(View.GONE);
        newLevelButton.setVisibility(View.GONE);
        newLevelButton.setEnabled(true);
        returnButton.setEnabled(true);

        setupPauseMenu(gameActivity);
    }

    private void setupPauseMenu(final GameActivity gameActivity){
        final FloatingActionButton pauseButton = new FloatingActionButton(gameActivity);
        pauseButton.setImageResource(android.R.drawable.ic_media_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(gameActivity, "Pause button pressed!", pauseButton.getId()).show();
            }
        });
        widgets.add(pauseButton);
    }

    public void drawUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.addView(widget);
            System.out.printf("Added %s\n", widget.toString());
        }
    }

    public void removeUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.removeView(widget);
            System.out.printf("Removed %s\n", widget.toString());
        }
    }

    public void revealUI(){
        newLevelButton.setVisibility(View.VISIBLE);
        returnButton.setVisibility(View.VISIBLE);

    }

    public void hideUI(){
        newLevelButton.setVisibility(View.INVISIBLE);
        returnButton.setVisibility(View.INVISIBLE);

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
