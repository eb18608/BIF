package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import processing.core.PShape;
import processing.core.PVector;

public class LoopObject extends ObjectiveObject {
    PShape innerBody;
    float innerBodyRotation;
    float outerBodyRotation;
    float[] colour = {255, 195, 0, 245};

    public LoopObject(GameSketch sketch, PShape outerBody, PShape innerBody, float x, float y, float z,
                          float scale, int id) {
        super(sketch, outerBody, x, y, z, scale, id);
        h = 140 * scale;
        w = 140 * scale;
        d = 140 * scale;
        this.innerBody = innerBody;
    }

    public PVector getCoords(){
        return this.coords;
    }

    public float getH(){
        return h;
    }

    public float getW() {
        return w;
    }

    public float getD() {
        return d;
    }

    void setColour(float r, float g, float b, float a) {
        colour[0] = r;
        colour[1] = g;
        colour[2] = b;
        colour[3] = a;
    }

    @Override
    public void draw3D() {
        //WE NEED THIS CODE FOR FUTURE REFERENCE
        //PLEASE DON'T DELETE

        this.body.setFill(sketch.color(colour[0], colour[1], colour[2], colour[3]));

        sketch.pushMatrix();
        sketch.translate(getCoords().x, getCoords().y, getCoords().z);
        sketch.pushMatrix();
        sketch.rotateY(outerBodyRotation);
        sketch.shape(body);
        outerBodyRotation -= 0.05f;
        sketch.popMatrix();
        sketch.pushMatrix();
        sketch.rotateY(innerBodyRotation);
        innerBodyRotation += 0.025f;
        sketch.shape(innerBody);
        sketch.popMatrix();
        sketch.popMatrix();



    }

    @Override
    public void draw2D() {
        if (sketch.distanceToDrone(this) + sketch.avg(this.getW()/2, this.getD()/2) < 1500) {
            sketch.pushMatrix();
            sketch.translate(this.getCoords().x/10 - this.getW()/20, -this.getCoords().z/10 - this.getD()/20);
            sketch.fill(sketch.color(colour[0], colour[1], colour[2], 100));
            sketch.circle(0, 0, this.getW()/5);
            sketch.popMatrix();
        }
    }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        System.out.println("YOU'VE HIT THIS CLASS");
        System.out.println(this.getClass());
        setStatus(true);
        System.out.println("PLEASE DON'T HIT THE CLASS");
        setColour(40, 255, 40, 245);

    }

    @Override
    public boolean isDrone() {
        return false;
    }
}
