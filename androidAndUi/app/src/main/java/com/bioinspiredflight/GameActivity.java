package com.bioinspiredflight;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.ControlMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.ui.InputToOutput;
import com.bioinspiredflight.ui.Ui;

import javax.vecmath.Vector3d;

import processing.android.PFragment;
import processing.android.CompatUtils;

public class GameActivity extends AppCompatActivity {
    private GameSketch sketch;
    private InputToOutput io;
    private Movement movingObject;
    private ControlMod controlMod;
    private SharedPreferences preferences;
    private CollideMod collideMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final FrameLayout frame = new FrameLayout(this);
        final RelativeLayout uiLayout = new RelativeLayout(this);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sliderToggle = preferences.getBoolean("switch_snap_slider", true);

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
        sketch = new GameSketch();
        this.io = new InputToOutput();
        Vector3d startPos = new Vector3d(0, 0, 0);
        this.movingObject = new Movement(1.0, true, startPos, sketch.drone.h, sketch.drone.w);
        final Ui ui = new Ui(this, io, sliderToggle);
        this.controlMod = new ControlMod(io);
        this.collideMod = new CollideMod(new Vector3d());
        PFragment pFragment = new PFragment(sketch);
        pFragment.setView(frame, this);
        ui.drawUi(uiLayout);
        sketch.setMovingObject(movingObject, controlMod, io, collideMod);
    }

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
}
