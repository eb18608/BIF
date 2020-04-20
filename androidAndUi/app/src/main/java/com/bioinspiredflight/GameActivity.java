package com.bioinspiredflight;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.ui.Ui;
import com.bioinspiredflight.utilities.LevelHandler;

import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PVector;

public class GameActivity extends AppCompatActivity {
    private GameSketch sketch;
    private InputToOutput io;
    private Movement movingObject;
    private ControlMod controlMod;
    private SharedPreferences preferences;
    private CollideMod collideMod;
    private LevelHandler levelHandler;
    boolean sliderToggle;
    FrameLayout frame;
    RelativeLayout uiLayout;
    Ui ui;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameSketchObserver obs = new GameSketchObserver();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sliderToggle = preferences.getBoolean("switch_snap_slider", true);
        frame = new FrameLayout(this);
        uiLayout = new RelativeLayout(this);



        frame.setId(CompatUtils.getUniqueViewId());
        uiLayout.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        addContentView(uiLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        this.io = new InputToOutput();
        sketch = new GameSketch();
        PVector startPos = new PVector(0, 0, 0);
        this.movingObject = new Movement(1.0f, false, startPos);
        ui = new Ui(this, io, sliderToggle);
        obs.setUi(ui);
        obs.setSketch(sketch);
        this.controlMod = new ControlMod(io);
        this.collideMod = new CollideMod(new PVector(0.0f,0.0f,0.0f));
        levelHandler = new LevelHandler(this);
        PFragment pFragment = new PFragment(sketch);
        pFragment.setView(frame, this);
        ui.drawUi(uiLayout);
        sketch.setMovingObject(movingObject, controlMod, io, collideMod);
        sketch.setLevelHandler(levelHandler);
    }

    @Override
    public void onPause() {
        System.out.println("pausing");
        finish();
        super.onPause();
    }

    /*public void onResume() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (sketch.setupCompleted) { sketch.resizeSky(); }
        super.onResume();
        ui.removeUi(uiLayout);
        ui.drawUi(uiLayout);
    }*/

    /*public void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROYING");
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }
    }

    @Override
    @CallSuper
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }

    @Override
    public void finish(){
        if (sketch.isLoaded() || sketch.isGamePaused()){
            System.out.println("bye");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public class GameSketchObserver {

        private Ui ui;
        private GameSketch sketch;

        public void updateUINewLevel(){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ui.setGameStatus(Ui.GameStatus.IN_PROGRESS);
                    ui.hideMenu();

                }
            });
            sketch.resume();
        }

        public void startLevelSelect(){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ui.showLevelSelect(true, sketch.getActivity());
                }
            });
            sketch.pause();
        }

        public void updateUiComplete(){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                  if(ui.getGameStatus() != Ui.GameStatus.COMPLETED && ui.getGameStatus() != Ui.GameStatus.LOADING){
                      ui.setGameStatus(Ui.GameStatus.COMPLETED);
                      ui.revealMenu();
                    }
                }
            });
            sketch.pause();
        }

        public void updateGameOver(){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(ui.getGameStatus() == Ui.GameStatus.IN_PROGRESS){
                        ui.setGameStatus(Ui.GameStatus.GAME_OVER);
                        ui.revealMenu();

                    }
                }
            });
            sketch.pause();
        }

        public void updateGameSketch(String levelFileName){
            sketch.startLevel(levelFileName);
            sketch.resume();
        }

        public void setUi(Ui ui) {
            this.ui = ui;
            ui.setObs(this);
        }

        public void setSketch(GameSketch sketch) {
            this.sketch = sketch;
            sketch.setObs(this);
        }

        public boolean togglePauseSketch(){
            boolean paused;
            if (sketch.isGamePaused()){
                sketch.resume();
                paused = false;
            } else {
                sketch.pause();
                paused = true;
            }
            return paused;
        }

        public void startTimer(final long seconds){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ui.startTimer(seconds);
                }
            });
        }

        public void updateUiTimer(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ui.updateTimer();
                }
            });
        }
    }
}
