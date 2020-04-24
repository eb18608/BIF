package com.bioinspiredflight.physicsTests;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.ApartmentsObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

import org.junit.Test;

import processing.core.PShape;
import processing.core.PVector;

import static org.junit.Assert.*;

public class MovementTest {
    GameSketch sketch = new GameSketch();
    float scale = 1f;
    PShape body = new PShape();
    int id = 1;

    @Test
    public void forceAppliedTest() {
        //Set the comparison parameters
        PVector inputForce = new PVector(0.8f, 0.5f, 0f);
        PVector expectedAcc = new PVector(240f, 150f, 0f);
        PVector result;
        float delta = 0.1f; // Leeway

        //Set context
        PVector initPos = new PVector(0, 0, 0);
        PVector initAcc = new PVector(1 ,1, 1);
        Movement movement = new Movement(1f, true, initAcc);

        result = movement.forceApplied(initAcc, inputForce, movement.getMass(), movement.frametime);
        assertEquals(expectedAcc.x, result.x, delta);
        assertEquals(expectedAcc.y, result.y, delta);
        assertEquals(expectedAcc.z, result.z, delta);
    }

    @Test
    public void calcVelTest() {
        //Set comparison parameters
        PVector inputAcc = new PVector(240f, 150f, 0f);
        PVector expectedVel = new PVector(8f, 5f, -6f);
        PVector result;
        float delta = 0.1f;
        //Set Context
        PVector initAcc = new PVector(1 ,1, 1);
        Movement movement = new Movement(1f, true, initAcc);

        result = movement.calcVel(movement.getVel(), inputAcc, movement.frametime);
        assertEquals(expectedVel.x, result.x, delta);
        assertEquals(expectedVel.y, result.y, delta);
        assertEquals(expectedVel.z, result.z, delta);
    }

    @Test
    public void calcPosTest() {
        //Set comparison parameters
        PVector inputVel = new PVector(8f, 5f, -6f);
        PVector expectedPos = new PVector(0.27f, 0.17f, -0.2f);
        PVector result;
        float delta = 0.1f;
        //Set Context
        PVector initAcc = new PVector(1 ,1, 1);
        Movement movement = new Movement(1f, true, initAcc);

        result = movement.calcPos(movement.getPos(), inputVel, movement.frametime);
        assertEquals(expectedPos.x, result.x, delta);
        assertEquals(expectedPos.y, result.y, delta);
        assertEquals(expectedPos.z, result.z, delta);
    }

    @Test
    public void updateMoverTest() {
        //Comparison parameters
        PVector expectedAcc = new PVector(240f, 150f, 0f);
        PVector expectedVel = new PVector(6f, 3f, -4f);
        PVector expectedPos = new PVector(0.27f, 0.17f, 0f);
        float delta = 0.1f;

        //Set context
        PVector initAcc = new PVector(1 ,1, 1);
        Movement movement = new Movement(1f, true, initAcc);
        movement.updateMover(expectedAcc, expectedVel, expectedPos, movement);

        assertEquals(expectedAcc.x, (movement.getAcc()).x, delta);
        assertEquals(expectedAcc.y, (movement.getAcc()).y, delta);
        assertEquals(expectedAcc.z, (movement.getAcc()).z, delta);

        assertEquals(expectedVel.x, (movement.getVel()).x, delta);
        assertEquals(expectedVel.y, (movement.getVel()).y, delta);
        assertEquals(expectedVel.z, (movement.getVel()).z, delta);

        assertEquals(expectedPos.x, (movement.getPos()).x, delta);
        assertEquals(expectedPos.y, (movement.getPos()).y, delta);
        assertEquals(expectedPos.z, (movement.getPos()).z, delta);
    }

    @Test
    public void limitVelocityTest() {
        //Comparison Parameters
        PVector expectedVel = new PVector(200f, 200f, -250f);
        PVector result;
        float delta = 0.1f;

        //Set context
        PVector initAcc = new PVector(10f ,10f, -10f);
        PVector initVel = new PVector(200f, 200f, -250f);
        Movement movement = new Movement(1f, true, initAcc);
        movement.setVel(initVel);
        result = movement.calcVel(initAcc, initVel, movement.frametime);
        movement.limitVelocity(initAcc, initVel);
        result = movement.getVel();

        assertEquals(expectedVel.x, movement.getVel().x, delta);
        assertEquals(expectedVel.y, movement.getVel().y, delta);
        assertEquals(expectedVel.z, movement.getVel().z, delta);


    }

    @Test
    public void airResistanceTest() {
        //Comparison points
        PVector exepectedVel = new PVector(198f, 198f, -248f);
        PVector result;
        //Set context
        PVector initAcc = new PVector(10f ,10f, -10f);
        PVector initVel = new PVector(200f, 200f, -250f);
        Movement movement = new Movement(1f, true, initAcc);
        PVector initPos = new PVector(500, 500, 500);

        movement.updateMover(initAcc, initVel, initPos, movement);
        result = movement.getVel();

        assertEquals(exepectedVel.x, result.x, movement.frametime);
        assertEquals(exepectedVel.y, result.y, movement.frametime);
        assertEquals(exepectedVel.z, result.z, movement.frametime);

    }
    //Not finished yet
    @Test
    public void collisionTrueTest() {
        //Set context
        int scale = 1;
        int id = 1;
        PVector initAcc = new PVector(0f ,0f, 0f);
        Movement movement = new Movement(1f, true, initAcc);

        PVector initPos = new PVector(2543f, 3500f,500f );
        movement.setPos(initPos);
        DroneObject droneObject = new DroneObject(initPos.x, initPos.y, initPos.z, scale, id);
        movement.setMovementSize(droneObject);
        ApartmentsObject obj = new ApartmentsObject(4000f, 660f, 4000f, 1, 1, 1);


        System.out.println(movement.collisionDetectorXY(movement, obj));
        movement.isCollision(movement, obj);
        //        System.out.println(movement.collided);
        assertTrue(movement.collided);

    }

    @Test
    public void collisionFalseTest() {
        //Set context
        int scale = 1;
        int id = 1;
        PVector initAcc = new PVector(0f ,0f, 0f);
        Movement movement = new Movement(1f, true, initAcc);

        PVector initPos = new PVector(2542f, 3500f,500f );
        movement.setPos(initPos);
        DroneObject droneObject = new DroneObject(initPos.x, initPos.y, initPos.z, scale, id);
        movement.setMovementSize(droneObject);
        ApartmentsObject obj = new ApartmentsObject(4000f, 660f, 4000f, 1, 1, 1);
        movement.isCollision(movement, obj);
        //        System.out.println(movement.collided);
        assertFalse(movement.collided);

    }

}