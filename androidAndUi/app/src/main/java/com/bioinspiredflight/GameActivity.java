package com.bioinspiredflight;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import com.bioinspiredflight.ui.Ui;

import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;

public class GameActivity extends AppCompatActivity {
    private PApplet sketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final FrameLayout frame = new FrameLayout(this);
        final RelativeLayout uiLayout = new RelativeLayout(this);
        //final Ui ui = new Ui(frame, this);
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
        sketch = new Placeholder();
        PFragment pFragment = new PFragment(sketch);
        pFragment.setView(frame, this);
        final Ui ui = new Ui(this);
        ui.drawUi(uiLayout);
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
