package com.bioinspiredflight;

import processing.core.PApplet;

/**
 * This is a placeholder class for Processing code.
 * The game is going to run from here.
 */
public class Placeholder extends PApplet{

    public void settings() {
        size(600, 600);
        //fullScreen();
    }

    public void setup() {

    }

    public void draw() {
        if (mousePressed) {
            ellipse(mouseX, mouseY, 50, 50);
        }
    }

}
