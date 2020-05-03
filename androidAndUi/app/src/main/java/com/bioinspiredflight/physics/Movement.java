/**The MIT License (MIT)
 *
 * Copyright © 2020 Bio-Inspired Flight Lab, University of Bristol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bioinspiredflight.physics;


import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;

import processing.core.PVector;

import static androidx.core.math.MathUtils.clamp;
import static java.lang.Math.sqrt;
/*Movement is my testing class for the visitor functions capabilities
Movement is meant to simulate any object in the game that will be affected by any form of movement interaction
If a player, then would have all the interactions (control from player input, environment obstacles or even wall
collisions*/


public class Movement {
    private float mass;
    private boolean gravityOn;
    private PVector pos;
    private PVector vel;
    private PVector acc;
    private final float framerate = 30;
    public final float frametime = 1 / framerate;
    public boolean collided = false;
    public float radius;
    private float height;
    private boolean inStream;

    public Movement(float mass, boolean gravityOn, PVector p, float height,float radius){
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.acc = p;
        this.vel = new PVector(0.0f, 0.0f, 0.0f);
        this.pos = new PVector(0.0f, 0.0f, 0.0f);
        this.height = height;
        this.radius = radius;
    }
    public void setMovementSize(DroneObject drone){
        this.radius = drone.getDi()/2;
        this.height = drone.getH();
    }

    public Movement(float mass, boolean gravityOn, PVector p) {
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.acc = p;
        this.vel = new PVector(0.0f, 0.0f, 0.0f);
        this.pos = new PVector(0.0f, 0.0f, 0.0f);
    }

    public Movement(float mass, boolean gravityOn, PVector p, PVector v, PVector a) {
        this.mass = mass;
        this.gravityOn = gravityOn;
        this.pos = p;
        this.vel = v;
        this.acc = a;
    }

    //Get individual component from PVector
    public float getX(PVector p){
        return p.x;
    }
    public float getY(PVector p){
        return p.y;
    }
    public float getZ(PVector p){
        return p.z;
    }


    //Set individual componend in PVector
    public void setX(PVector p, float x){
        p.x = x;
    }
    public void setY(PVector p, float y){
        p.y = y;
    }
    public void setZ(PVector p, float z){
        p.z = z;
    }
    public void setV(PVector p, float x, float y, float z){
        p.x = x;
        p.y = y;
        p.z = z;
    }
    public boolean isInStream() {
        return inStream;
    }

    public void setInStream(boolean inStream) {
        this.inStream = inStream;
    }

//    //Get the individual components of the position vector to do math on them
//    public double getPosX() {
//        return pos.x;
//    }
//
//    public double getPosZ() {
//        return pos.z;
//    }
//
//    public double getPosY() {
//        return pos.y;
//    }
//
//    //Get the individual components of the velocity vector to do math on them
//    public double getVelX() {
//        return vel.x;
//    }
//
//    public double getVelZ() {
//        return vel.z;
//    }
//
//    public double getVelY() {
//        return vel.y;
//    }
//
//    //Get the individual components of the acceleration vector to do math on them
//    public double getAccX() {
//        return acc.x;
//    }
//
//    public double getAccZ() {
//        return acc.z;
//    }
//
//    public double getAccY() {
//        return acc.y;
//    }

    //Set the values for pos, vel, acc
    public PVector getAcc() {
        return acc;
    }

    public PVector getVel() {
        return vel;
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(PVector pos) {
        this.pos = pos;
    }

    public void setVel(PVector vel) {
        this.vel = vel;
    }

    public void setAcc(PVector acc) {
        this.acc = acc;
    }

    public void setMass(float m) {
        this.mass = m;
    }

    public float getMass() {
        return mass;
    }


    public PVector forceApplied(PVector currentAcc, PVector inputForce, float mass, float frametime) {
        float gravity = 0;
        float scale = 10000;
        PVector newAcc = new PVector(0.0f, 0.0f, 0.0f);
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

    public PVector calcVel(PVector currentVel, PVector accVector, float frametime) {
        PVector newVel = new PVector();
        float gravity = 10f;
        newVel.x = (currentVel.x + (frametime * accVector.x));
        newVel.y = (currentVel.y + (frametime * accVector.y));
        newVel.z = (currentVel.z + (frametime * accVector.z));

        if (gravityOn){
            newVel.z -= gravity;
        }

        return newVel;

    }

    public PVector calcPos(PVector currentPos, PVector velVector, float frametime) {
        PVector newPos = new PVector();

        newPos.x = (currentPos.x + (frametime * velVector.x));
        newPos.y = (currentPos.y + (frametime * velVector.y));
        newPos.z = (currentPos.z + (frametime * velVector.z));

        return newPos;
    }

    public void updateMover(PVector resultAcc, PVector resultVel, PVector resultPos, Movement thisMover) {
        if(inStream) {
            System.out.println("In stream");

            thisMover.acc = resultAcc;
            airResistance(resultAcc, resultVel);
            thisMover.vel = resultVel;
            thisMover.pos = resultPos;
            setInStream(false);
        } else{
            thisMover.acc = resultAcc;
            limitVelocity(resultAcc, resultVel);
            airResistance(resultAcc, resultVel);
            thisMover.vel = resultVel;
            thisMover.pos = resultPos;
        }

        floorLock(resultVel, resultPos);

    }

    public void limitVelocity(PVector acc, PVector vel) {
        final float maxVelX = 200;
        final float maxVelY = 200;
        final float maxVelZ = 200;
        final float terminalVel = 250;
        if (getX(vel) > maxVelX) {
            setX(vel ,maxVelX);
        } else if (getX(vel) < -maxVelX) {
            setX(vel ,-maxVelX);
        }
        if (getY(vel) > maxVelY) {
            setY(vel, maxVelY);
        } else if (getY(vel) < -maxVelY) {
            setY(vel, -maxVelY);
        }
        if (getZ(vel) > maxVelZ) {
            setZ(vel, maxVelZ);
        } else if (getZ(vel) < -maxVelZ && getZ(acc) < 0) {
            if (gravityOn){
                setZ(vel, -terminalVel);
            } else {
                setZ(vel, -maxVelZ);
            }
        }
    }

    public void airResistance(PVector acc, PVector vel) {
        float velX = getX(vel);
        float velY = getY(vel);
        float velZ = getZ(vel);
        final float decel = 2f;
        float newVel;
        if (velX > 0) {
            newVel = velX - decel;
            if (velX < 0) {
                newVel = 0;
            }
            setX(vel, newVel);
        } else {
            newVel = velX + decel;
            if (newVel > 0) {
                newVel = 0;
            }
            setX(vel, newVel);
        }
        if (velY > 0) {
            newVel = velY - decel;
            if (velY < 0) {
                newVel = 0;
            }
            setY(vel, newVel);
        } else {
            newVel = velY + decel;
            if (newVel > 0) {
                newVel = 0;
            }
            setY(vel, newVel);
        }
        if (velZ > 0) {
            newVel = velZ - decel;
            if (velZ < 0) {
                newVel = 0;
            }
            setZ(vel, newVel);
        } else {
            newVel = velZ + decel;
            if (newVel > 0) {
                newVel = 0;
            }
            setZ(vel, newVel);
        }
    }

    public void floorLock(PVector vel, PVector pos) {
        if (getZ(pos) < 11.5f && getZ(vel) < 0) {
            setZ(vel,0);
            setZ(pos,11.5f);
        }
    }

    //Circle to box collisions only happen at X and Z (Z is the our Y co-ordinate)

    public boolean  collisionDetectorXY(Movement drone, GameObject object2){
        //Create Vectors for: Centre for drone and building, differences, clamps
        PVector droneCentre = new PVector(0.0f, 0.0f);
        PVector objectCentre = new PVector(0.0f, 0.0f);
        PVector objectBounds = new PVector(0.0f, 0.0f);
        PVector centreDifference = new PVector(0.0f, 0.0f);
        PVector clampedDifference = new PVector(0.0f, 0.0f);
        PVector closestPoint = new PVector(0.0f, 0.0f);

        //Assign Drone centre:
        droneCentre.x = drone.getX(pos);
        droneCentre.y = drone.getY(pos);


        //Assign Object's centre:
        objectCentre.x = object2.getCoords().x;
        objectCentre.y = object2.getCoords().z;


        //Set object's bounds:
        objectBounds.x = object2.getW()/2;
        objectBounds.y = object2.getD()/2;


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

        boolean overlap = lengthOfVector(centreDifference) <= radius;
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

    public boolean collisionDetectorZ(Movement drone, GameObject object2){
        double droneZmax = drone.getZ(pos) + drone.height/2;
        double objectZmax = object2.getCoords().y + object2.getH()/2;

        double droneZmin = drone.getZ(pos) - drone.height/2;
        double objectZmin = object2.getCoords().y - object2.getH()/2;

        boolean overlapZ;
        if(droneZmax > objectZmin && droneZmax < objectZmax){
            overlapZ = true;
        } else if (droneZmin < objectZmax && droneZmin > objectZmin){
            overlapZ = true;
        }
        else overlapZ = false;

        return overlapZ;
    }

    public float clamp(float x, float min, float max){
        return Math.max(min, Math.min(max, x));
    }

    public double lengthOfVector(PVector vec){
        float length = (float)sqrt((vec.x*vec.x) + (vec.y*vec.y));
        return length;
    }

    public boolean isCollision(Movement drone, GameObject object2){
        if(collisionDetectorXY(drone,object2) && collisionDetectorZ(drone, object2)){
            if (object2.isSolid()){
                drone.collided = true;
            }
            return true;
        }else{
            //drone.collided = false;
            return false;
        }
    }



//    public void collide(Movement drone, GameSketch.buildingObject object2){
//        if (drone.collided == true){
//            setVel(new Vector3d(0, 0, 0));
//        }
}
