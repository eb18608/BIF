package com.bioinspiredflight.physics;


import com.bioinspiredflight.GameSketch;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import static androidx.core.math.MathUtils.clamp;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.max;
import static java.lang.Double.min;
import static java.lang.Math.sqrt;
import static java.sql.DriverManager.println;
/*Movement is my testing class for the visitor functions capabilities
Movement is meant to simulate any object in the game that will be affected by any form of movement interaction
If a player, then would have all the interactions (control from player input, environment obstacles or even wall
collisions*/


public class Movement {
    private double mass;
    private boolean gravityOn;
    private Vector3d pos;
    private Vector3d vel;
    private Vector3d acc;
    private final double framerate = 30;
    public final double frametime = 1 / framerate;
    public boolean collided;
    public double radius;
    private double height;

    public Movement(double mass, boolean gravityOn, Vector3d p,float height,float radius){
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.acc = p;
        this.vel = new Vector3d();
        this.pos = new Vector3d();
        this.height = (double)height;
        this.radius = (double) radius;
    }

    public Movement(double mass, boolean gravityOn, Vector3d p) {
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.acc = p;
        this.vel = new Vector3d();
        this.pos = new Vector3d();
    }

    public Movement(double mass, boolean gravityOn, Vector3d p, Vector3d v, Vector3d a) {
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.pos = p;
        this.vel = v;
        this.acc = a;
    }

    //Get the individual components of the position vector to do math on them
    public double getPosX() {
        return pos.x;
    }

    public double getPosZ() {
        return pos.z;
    }

    public double getPosY() {
        return pos.y;
    }

    //Get the individual components of the velocity vector to do math on them
    public double getVelX() {
        return vel.x;
    }

    public double getVelZ() {
        return vel.z;
    }

    public double getVelY() {
        return vel.y;
    }

    //Get the individual components of the acceleration vector to do math on them
    public double getAccX() {
        return acc.x;
    }

    public double getAccZ() {
        return acc.z;
    }

    public double getAccY() {
        return acc.y;
    }

    //Set the values for pos, vel, acc
    public Vector3d getAcc() {
        return acc;
    }

    public Vector3d getVel() {
        return vel;
    }

    public Vector3d getPos() {
        return pos;
    }

    public void setPos(Vector3d pos) {
        this.pos = pos;
    }

    public void setVel(Vector3d vel) {
        this.vel = vel;
    }

    public void setAcc(Vector3d acc) {
        this.acc = acc;
    }

    public void setMass(int m) {
        this.mass = m;
    }

    public double getMass() {
        return mass;
    }


    public Vector3d forceApplied(Vector3d currentAcc, Vector3d inputForce, double mass, double frametime) {
        double gravity = 0;
        double scale = 500;
        //System.out.println(inputForce.toString());
        Vector3d newAcc = new Vector3d();
        if (gravityOn) {
            newAcc.x = ((frametime * scale * inputForce.x)) / mass;
            newAcc.y = ((frametime * scale * inputForce.y)) / mass;
            newAcc.z = (((frametime * scale * inputForce.z)) / mass) - gravity;

        } else {
            newAcc.x = ((frametime * scale * inputForce.x)) / mass;
            newAcc.y = ((frametime * scale * inputForce.y)) / mass;
            newAcc.z = ((frametime * scale * inputForce.z)) / mass;

        }

        return newAcc;
    }

    public Vector3d calcVel(Vector3d currentVel, Vector3d accVector, double frametime) {
        Vector3d newVel = new Vector3d();
        double gravity = 0.3;
        newVel.x = (currentVel.x + (frametime * accVector.x));
        newVel.y = (currentVel.y + (frametime * accVector.y));
        newVel.z = (currentVel.z + (frametime * accVector.z));

        if (gravityOn){
            newVel.z -= gravity;
        }

        return newVel;

    }

    public Vector3d calcPos(Vector3d currentPos, Vector3d velVector, double frametime) {
        Vector3d newPos = new Vector3d();

        newPos.x = (currentPos.x + (frametime * velVector.x));
        newPos.y = (currentPos.y + (frametime * velVector.y));
        newPos.z = (currentPos.z + (frametime * velVector.z));

        return newPos;
    }

    public void updateMover(Vector3d resultAcc, Vector3d resultVel, Vector3d resultPos, Movement thisMover) {

        thisMover.acc = resultAcc;
        limitVelocity(resultAcc, resultVel);
        airResistance(resultAcc, resultVel);
        thisMover.vel = resultVel;
        thisMover.pos = resultPos;


        floorLock(resultVel, resultPos);
        //System.out.printf("Movement stuff\n");
        //System.out.printf("Acc: %s\n", this.acc.toString());
        //System.out.printf("Vel: %s\n", this.vel.toString());
        //System.out.printf("Pos: %s\n", this.pos.toString());
    }

    public void limitVelocity(Vector3d acc, Vector3d vel) {
        double maxVelX = 5;
        double maxVelY = 5;
        double maxVelZ = 5;
        double terminalVel = 6;
        if (vel.getX() > maxVelX) {
            vel.setX(maxVelX);
        } else if (vel.getX() < -maxVelX) {
            vel.setX(-maxVelX);
        }
        if (vel.getY() > maxVelY) {
            vel.setY(maxVelY);
        } else if (vel.getY() < -maxVelY) {
            vel.setY(-maxVelY);
        }
        if (vel.getZ() > maxVelZ) {
            vel.setZ(maxVelZ);
        } else if (vel.getZ() < -maxVelZ && acc.getZ() <= 0) {
            if (gravityOn){
                vel.setZ(-terminalVel);
            } else {
                vel.setZ(-maxVelZ);
            }
        }
    }

    public void airResistance(Vector3d acc, Vector3d vel) {
        double velX = vel.getX();
        double velY = vel.getY();
        double velZ = vel.getZ();
        double newVel;
        if (velX > 0) {
            newVel = velX - 0.1;
            if (velX < 0) {
                newVel = 0;
            }
            vel.setX(newVel);
        } else {
            newVel = velX + 0.1;
            if (newVel > 0) {
                newVel = 0;
            }
            vel.setX(newVel);
        }
        if (velY > 0) {
            newVel = velY - 0.1;
            if (velY < 0) {
                newVel = 0;
            }
            vel.setY(newVel);
        } else {
            newVel = velY + 0.1;
            if (newVel > 0) {
                newVel = 0;
            }
            vel.setY(newVel);
        }
        if (velZ > 0) {
            newVel = velZ - 0.1;
            if (velZ < 0) {
                newVel = 0;
            }
            vel.setZ(newVel);
        } else {
            newVel = velZ + 0.1;
            if (newVel > 0) {
                newVel = 0;
            }
            vel.setZ(newVel);
        }
    }

    public void floorLock(Vector3d vel, Vector3d pos) {
        if (pos.getZ() < 0 && vel.getZ() < 0) {
            vel.setZ(0);
            pos.setZ(0);
        }
    }

    //Circle to box collisions only happen at X and Z (Z is the our Y co-ordinate)

    public boolean collisionDetectorXY(Movement drone, GameSketch.buildingObject object2){
        //Create Vectors for: Centre for drone and building, differences, clamps
        Vector2d droneCentre = new Vector2d();
        Vector2d objectCentre = new Vector2d();
        Vector2d objectBounds = new Vector2d();
        Vector2d centreDifference = new Vector2d();
        Vector2d clampedDifference = new Vector2d();
        Vector2d closestPoint = new Vector2d();

        //Assign Drone centre:
        droneCentre.x = drone.getPosX();
        droneCentre.y = drone.getPosY();

        //Assign Object's centre:
        objectCentre.x = object2.coords.x;
        objectCentre.y = object2.coords.z;

        //Set object's bounds:
        objectBounds.x = object2.w/2;
        objectBounds.y = object2.d/2;

        //Get difference vector (of centre's)
        centreDifference.x = droneCentre.x - objectCentre.x;
        centreDifference.y = droneCentre.y - objectCentre.y;

        //Set clamped value
        clampedDifference.x = clamp(centreDifference.x, -1*objectBounds.x, objectBounds.x);
        clampedDifference.y = clamp(centreDifference.y, -1*objectBounds.y, objectBounds.y);

        //Set closest point from box's bounds to centre
        closestPoint.x = objectCentre.x + clampedDifference.x;
        closestPoint.y = objectCentre.y + clampedDifference.y;

        centreDifference.x = droneCentre.x - closestPoint.x;
        centreDifference.y = droneCentre.y - closestPoint.y;

        boolean overlap = lengthOfVector(centreDifference) < radius;
        return overlap;


////      println("Called XY Overlap checker");
//
//        //Declaring lots of vectors
//        //Centre
//        Vector2d droneCentre = new Vector2d();
//        Vector2d object2Centre = new Vector2d();
//        //Difference and Clamp is where we will measure closeness
//        Vector2d difference = new Vector2d();
//        Vector2d clampX = new Vector2d();
//        Vector2d clampY = new Vector2d();
//
//        Vector2d closestPoint = new Vector2d();
//        //Bound of the box
//        Vector2d object2Bound = new Vector2d();
//
//        //Set droneCentre (position of drone)
//        droneCentre.x = drone.pos.x;
//        droneCentre.y = drone.pos.y;
//        println("Position of Drone: " + droneCentre);
//        println("Position of building: " + object2Centre);
//        //Set object2's centre (position of building maybe)
//        object2Centre.x = object2.
//        object2Centre.y = object2.
//        //Half-width of object (usually for building)
//        object2Bound.x = (double)object2.getWidth()/2;
//        object2Bound.y = (double)object2.getDepth()/2;
//        //Difference between 2 centres of drone and object
//        difference.x = droneCentre.x - object2Centre.x;
//        difference.y = droneCentre.y - object2Centre.y;
//        println("Difference in positioning: " + difference);
//        //Difference between them but bounded by the dimensions of the object
//        clampX.x = clamp(difference.x, object2Bound.x, (-1*object2Bound.x));
//        clampX.y = clamp(difference.y, object2Bound.y, (-1*object2Bound.y));
//        //Point on object 2's bound which is closest to drone
//        closestPoint.add(object2Centre ,clampX);
//        //Calculating displacement vector between drone and closest point
//        difference.x = droneCentre.x - closestPoint.x;
//        difference.y = droneCentre.y - closestPoint.y;
//        //Absolute distance of drone to closest point
//        double lengthOfDiff = lengthVector2d(difference);
//
//        return lengthOfDiff < drone.width;
    }

    public boolean collisionDetectorZ(Movement drone, GameSketch.buildingObject object2){
        double droneZmax = drone.getPosZ() + drone.height/2;
        double objectZmax = object2.coords.y + object2.h/2;

        double droneZmin = drone.getPosZ() - drone.height/2;
        double objectZmin = object2.coords.y - object2.h/2;

        boolean overlapZ;
        if(droneZmax > objectZmin && droneZmax < objectZmax){
            overlapZ = true;
        } else if (droneZmin < objectZmax && droneZmin > objectZmin){
            overlapZ = true;
        }
        else overlapZ = false;

        return overlapZ;
    }

    public double clamp(double x, double min, double max){
        return max(min, min(max, x));
    }

    public double lengthOfVector(Vector2d vec){
        double length = sqrt((vec.x*vec.x) + (vec.y*vec.y));
        return length;
    }

    public void isCollision(Movement drone, GameSketch.buildingObject object2){
        if(collisionDetectorXY(drone,object2) && collisionDetectorZ(drone, object2)){
            drone.collided = true;

        }else{
            drone.collided = false;
        }
    }



}
