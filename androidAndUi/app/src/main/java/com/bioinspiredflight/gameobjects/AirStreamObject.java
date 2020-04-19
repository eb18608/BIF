package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;
import com.bioinspiredflight.sensor.SensorContent;

import processing.core.PShape;
import processing.core.PVector;

public class AirStreamObject extends GameObject {
    PVector direction;
    public AirStreamObject(GameSketch sketch, PShape body, float x, float y, float z,
                           float scale, float roty, int id, float rotx, float rotz) {
        super(sketch, body, x, y, z, id);
        h = 600 * scale;
        w = 120 * scale;
        d = 120 * scale;
        direction.y = roty;
        direction.x = rotx;
        direction.z = rotz;
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

    @Override
    public void draw3D() {
        if (SensorContent.ITEMS.get(0).isEquipped()) {
            sketch.pushMatrix();
            sketch.translate(getCoords().x, getCoords().y, getCoords().z);
            sketch.shape(body);
            sketch.popMatrix();
        }
    }

    @Override
    public void draw2D() { }

    @Override
    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch) {
        PVector streamVector = new PVector();
        float strength = 10f;
        streamVector.x = direction.x * strength;
        streamVector.y = direction.y * strength;
        streamVector.z = direction.z * strength;

        PVector resultantAcc = movement.forceApplied(
                movement.getAcc(),
                streamVector,
                movement.getMass(),
                movement.frametime);
        PVector resultantVel = movement.calcVel(
                movement.getVel(),
                resultantAcc,
                movement.frametime);

        PVector resultantPos = movement.calcPos(
                movement.getPos(),
                resultantVel,
                movement.frametime);



        movement.updateMover(resultantAcc, resultantVel, resultantPos, movement);
    }

    @Override
    public boolean isDrone() {
        return false;
    }

}

