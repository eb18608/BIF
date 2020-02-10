package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;

import javax.vecmath.Vector3d;

public class ModVisitor implements Visitor{
    @Override
    public void visit(ControlMod control, Movement movement) {
        //System.out.println(control.controlMod.toString());
        //System.out.println(control.controlMod);
        //System.out.printf("Input: %s\n", control.controlMod.toString());
        //System.out.printf("X: %.3f, Y: %.3f, Z: %.3f\n", movement.getAcc().getX(), movement.getAcc().getY(), movement.getAcc().getZ());
        Vector3d resultantAcc = movement.forceApplied(
                movement.getAcc(),
                control.controlMod,
                movement.getMass(),
                movement.frametime);
        //System.out.printf("Result: %s\n", resultantAcc.toString());
        Vector3d resultantVel = movement.calcVel(
                movement.getVel(),
                resultantAcc,
                movement.frametime);

        Vector3d resultantPos = movement.calcPos(
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

    }

    @Override
    public void visit(ControlMod control, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {
        //System.out.println(control.controlMod.toString());
        //System.out.println(control.controlMod);
        //System.out.printf("Input: %s\n", control.controlMod.toString());
        //System.out.printf("X: %.3f, Y: %.3f, Z: %.3f\n", movement.getAcc().getX(), movement.getAcc().getY(), movement.getAcc().getZ());
        Vector3d resultantAcc = movement.forceApplied(
                movement.getAcc(),
                control.controlMod,
                movement.getMass(),
                movement.frametime);
        //System.out.printf("Result: %s\n", resultantAcc.toString());
        Vector3d resultantVel = movement.calcVel(
                movement.getVel(),
                resultantAcc,
                movement.frametime);

        Vector3d resultantPos = movement.calcPos(
                movement.getPos(),
                resultantVel,
                movement.frametime);
        movement.collisionDetectorXY(movement, buildingObject);
        movement.collisionDetectorZ(movement, buildingObject);
        movement.isCollision(movement, buildingObject);

        movement.updateMover(resultantAcc, resultantVel, resultantPos, movement);
    }

    @Override
    public void visit(EnviroMod enviro, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {

    }

    //    @Override
//    public void visit(EnviroMod enviro, Movement movement) {
//        Vector3d resultantAcc = movement.forceApplied(
//                movement.getAcc(),
//                enviro.environMod,
//                movement.getMass(),
//                movement.frametime);
//        Vector3d resultantVel = movement.calcVel(
//                movement.getVel(),
//                resultantAcc,
//                movement.frametime);
//        Vector3d resultantPos = movement.calcPos(
//                movement.getPos(),
//                resultantVel,
//                movement.frametime);
//
//        movement.updateMover(resultantAcc, resultantVel, resultantPos, movement);
//    }
    @Override
    public void visit(EnviroMod enviro) {
    }
    @Override
    public void visit(ControlMod control) {
    }
    @Override
    public void visit(CollideMod collide) {
    }
//    @Override
//    public void visit(CollideMod collide, Movement movement) {
//        Vector3d resultantAcc = movement.forceApplied(
//                movement.getAcc(),
//                collide.collideMod,
//                movement.getMass(),
//                movement.frametime);
//        Vector3d resultantVel = movement.calcVel(
//                movement.getVel(),
//                resultantAcc,
//                movement.frametime);
//        Vector3d resultantPos = movement.calcPos(
//                movement.getPos(),
//                resultantVel,
//                movement.frametime);
//
//        movement.updateMover(resultantAcc, resultantVel, resultantPos, movement);
//    }

//    @Override
//    public void visit(ControlMod control, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {
//
//    }
//
//    @Override
//    public void visit(EnviroMod enviro, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {
//
//    }

    @Override
    public void visit(CollideMod collide, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone) {
        movement.setVel(collide.collideMod);
    }


}
