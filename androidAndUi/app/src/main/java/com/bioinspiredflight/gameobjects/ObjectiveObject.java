package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import java.io.SyncFailedException;

import processing.core.PShape;

public class ObjectiveObject extends GameObject {
    boolean status = false;
    int id;
//    int objArrayIndex;

    public ObjectiveObject(GameSketch sketch, PShape body, float x, float y, float z,
                          float scale, int id) {
        super(sketch, body, x, y, z, id);
        //objectFileName = "textured_drone_sans_propellers.obj";
        //loadShape();
        h = 600 * scale;
        w = 400 * scale;
        d = 400 * scale;
    }

    public Boolean getStatus(){
        return this.status;
    }
    public void setStatus(Boolean complete){
        this.status = complete;
    }
    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        //System.out.println("Collided with object");
        setStatus(true);
        //System.out.println("Status of Objective: "+ id + " is: " + status);
        Boolean done = sketch.checkCompleted();
        if(done){
            //System.out.println("WINNER WINNER CHICKEN DINNER");
        }
    }

    @Override
    public boolean isDrone() {
        return false;
    }

    @Override
    public void draw3D() {

    }

    @Override
    public void draw2D() {

    }
}
