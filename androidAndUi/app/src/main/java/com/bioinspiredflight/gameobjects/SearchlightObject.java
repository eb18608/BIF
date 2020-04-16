package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class SearchlightObject extends GameObject {
    PShape lightHolderBody, propellerFL, propellerFR, propellerBL, propellerBR;

    public SearchlightObject(GameSketch sketch, PShape body, float x, float y, float z,
                             float scale, int id) {
        super(sketch, body, x, y, z, id);
        if (SensorContent.ITEMS.get(2).isEquipped()) {
            scale *= 0.75f;
        }
        h = 400;
        w = 400 * scale;
        d = 400 * scale;

        lightHolderBody = sketch.loadShape("textured_drone_sans_propellers.obj");
        propellerFL = loadPropeller(scale);
        propellerFR = loadPropeller(scale);
        propellerBL = loadPropeller(scale);
        propellerBR = loadPropeller(scale);
    }

    private PShape loadPropeller(float scale) {
        PShape shape = sketch.loadShape("textured_propeller.obj");
        shape.scale(scale);
        return shape;
    }

    public PVector getCoords(){ return this.coords; }

    public float getH(){ return h; }

    public float getW() { return w; }

    public float getD() { return d; }

    private void spinProps(float multiplier) {
        sketch.pushMatrix();
        this.propellerFL.rotateY(multiplier);
        this.propellerFR.rotateY(-multiplier);
        this.propellerBL.rotateY(-multiplier);
        this.propellerBR.rotateY(multiplier);
        sketch.popMatrix();
    }

    @Override
    public void draw3D() {
        final float propellerXZ = 22;
        final float propellerY = 2;

        spinProps(0.3f);

        sketch.pushMatrix();

        sketch.translate(getCoords().x, getCoords().y, getCoords().z);
        //sketch.shape(body);

        sketch.pushMatrix();
        sketch.translate(0, getH()/2 + 10, 0);
        sketch.shape(lightHolderBody);

        sketch.pushMatrix();
        sketch.translate(-propellerXZ, propellerY, propellerXZ);
        sketch.shape(propellerFL);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(propellerXZ, propellerY, propellerXZ);
        sketch.shape(propellerFR);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(-propellerXZ, propellerY, -propellerXZ);
        sketch.shape(propellerBL);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.translate(propellerXZ, propellerY, -propellerXZ);
        sketch.shape(propellerBR);
        sketch.popMatrix();
        sketch.popMatrix();

        // Temp building to show location lol
        sketch.translate(-getW()/2,  -getH()/2,  -getD()/2);
        sketch.tint(255, 100);
        sketch.renderBuilding(this.getW(), this.getH(), this.getD());
        sketch.noTint();

        sketch.popMatrix();

    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        sketch.setLastPosition(movement.getPos());
        //TODO: Game over. "You were seen!"
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}
