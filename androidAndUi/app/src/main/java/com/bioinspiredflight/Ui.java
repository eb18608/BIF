package com.bioinspiredflight;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ui {

    private final Button testButton;
    private final FrameLayout frame;

    public Ui(FrameLayout frame, final AppCompatActivity gameActivity){
        this.frame = frame;
        testButton = new Button(gameActivity);
        testButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        testButton.setText("Test Button");
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(gameActivity.getApplicationContext(), "It works!",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void drawUi(){
        frame.addView(testButton);
    }

}
