package com.bioinspiredflight.physics;

import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.GameSketch;

import processing.core.PVector;

public class ModVisitor implements Visitor{
    @Override
    public void visit(ControlMod control, Movement movement) {
        System.out.println("Input force: ");
        System.out.println(control.controlMod);
        PVector resultantAcc = movement.forceApplied(
                movement.getAcc(),
                control.controlMod,
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
    public void visit(EnviroMod enviro, Movement movement) {

    }

    @Override
    public void visit(CollideMod collide, Movement movement) {
        movement.setVel(collide.collideMod);
    }

    @Override
    public void visit(ControlMod control, Movement movement, GameSketch sketch) {

    }

    @Override
    public void visit(CollideMod collide, Movement movement, GameSketch sketch) {
        movement.setVel(collide.collideMod);
        movement.setPos(sketch.getLastPosition());
    }

    @Override
    public void visit(CollideMod collide, Movement movement, GameSketch sketch, GameObject gameObject) {
        if (gameObject.isCollisionsEnabled()){
            gameObject.collide(collide, movement, sketch);
        }
    }

    @Override
    public void visit(EnviroMod enviro) {
    }

    @Override
    public void visit(ControlMod control) {
    }

    @Override
    public void visit(CollideMod collide) {
    }

}
