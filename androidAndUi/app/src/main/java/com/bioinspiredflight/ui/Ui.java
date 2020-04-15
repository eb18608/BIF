package com.bioinspiredflight.ui;

import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bioinspiredflight.GameActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

import processing.android.CompatUtils;

public class Ui {

    private PauseMenu pauseMenu;
    private ArrayList<View> widgets;
    private GameActivity.GameSketchObserver obs;
    private PlayerControls controls;
    //private final FrameLayout frame;
    //private final AppCompatActivity gameActivity;

    public Ui(final GameActivity gameActivity, InputToOutput io) {
        //layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity, io);
    }

    public Ui(final GameActivity gameActivity, InputToOutput io, boolean sliderToggle) {
        //layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity, io);

    }

    private void setupWidgets(final GameActivity gameActivity, InputToOutput io){
        DisplayMetrics metrics = new DisplayMetrics();
        gameActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widgets = new ArrayList<>();

        pauseMenu = new PauseMenu(gameActivity, widgets, metrics);
        controls = new PlayerControls(gameActivity, widgets, io);
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

    public void revealMenu(boolean levelCompleted){
        pauseMenu.revealMenu(levelCompleted);

    }

    public void hideMenu(){
        pauseMenu.hideMenu();

    }
    public void setObs(GameActivity.GameSketchObserver obs) {
        this.obs = obs;
    }

    public class PlayerControls {
        private Joystick testJoystick;
        private Slider testSlider;
        private InputToOutput io;
        private UiSurfaceView uiSurfaceView;

        public PlayerControls(GameActivity gameActivity, ArrayList<View> widgets, InputToOutput io){
            testJoystick = new Joystick(gameActivity);
            testJoystick.setId(CompatUtils.getUniqueViewId());
            testJoystick.addJoystickListener(new InputToOutput());
            widgets.add(testJoystick);
            testSlider = new Slider(gameActivity);
            testSlider.setId(CompatUtils.getUniqueViewId());
            widgets.add(testSlider);
            this.io = io;
            uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io);
            widgets.add(uiSurfaceView);
            this.io.setView(uiSurfaceView);
        }

    }

    public class PauseMenu {
        private TextView levelCompleteText;
        private Button returnButton;
        private Button newLevelButton;
        private FloatingActionButton pauseButton;

        public PauseMenu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            setupMenu(gameActivity, widgets, metrics);
            setupPauseButton(gameActivity, widgets, metrics);
        }

        private void setupMenu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            final int sideMargin = metrics.widthPixels / 4;
            final int topMargin = 3 * (metrics.heightPixels / 10);
            final int bottomMargin = 3 * (metrics.heightPixels / 10);
            final int buttonThickness = metrics.heightPixels / 5;

            levelCompleteText = new TextView(gameActivity);
            levelCompleteText.setText("LEVEL COMPLETE");
            RelativeLayout.LayoutParams textParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            textParams.leftMargin = sideMargin;
            textParams.rightMargin = sideMargin;
            textParams.topMargin = topMargin - buttonThickness;
            textParams.height = buttonThickness;
            levelCompleteText.setLayoutParams(textParams);

            newLevelButton = new Button(gameActivity);
            RelativeLayout.LayoutParams newLevelLayoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

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
            widgets.add(levelCompleteText);
            widgets.add(newLevelButton);
            widgets.add(returnButton);
            returnButton.setVisibility(View.GONE);
            newLevelButton.setVisibility(View.GONE);
            newLevelButton.setEnabled(true);
            returnButton.setEnabled(true);
        }

        private void setupPauseButton(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            pauseButton = new FloatingActionButton(gameActivity);
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(gameActivity, "Pause button pressed!", pauseButton.getId()).show();
                    boolean paused = obs.togglePauseSketch();
                    if (paused){

                        revealMenu(false);
                    } else {

                        hideMenu();
                    }
                    pauseButton.invalidate();
                }
            });
            RelativeLayout.LayoutParams pauseButtonParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            pauseButtonParams.leftMargin = metrics.widthPixels / 20;
            pauseButtonParams.topMargin = metrics.heightPixels / 20;
            pauseButton.setLayoutParams(pauseButtonParams);
            widgets.add(pauseButton);
        }

        public void revealMenu(boolean levelCompleted){
            if (levelCompleted){
                //pauseButton.hide();
                pauseButton.setEnabled(false);
                pauseButton.setImageResource(android.R.drawable.btn_radio);
                levelCompleteText.setVisibility(View.VISIBLE);
            } else {
                pauseButton.setImageResource(android.R.drawable.ic_media_play);
            }
            newLevelButton.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.VISIBLE);
            invalidateMenu();
        }

        public void hideMenu(){
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            //pauseButton.show();
            pauseButton.setEnabled(true);
            levelCompleteText.setVisibility(View.GONE);
            newLevelButton.setVisibility(View.GONE);
            returnButton.setVisibility(View.GONE);
            invalidateMenu();

        }

        // Makes sure everything in the menu is updated visually
        private void invalidateMenu(){
            levelCompleteText.invalidate();
            pauseButton.invalidate();
            newLevelButton.invalidate();
            returnButton.invalidate();
        }
    }

}
