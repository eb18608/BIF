package com.bioinspiredflight.physics;

import javax.vecmath.Vector3d;
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
        double gravity = 9.81;
        double scale = 500;
        //System.out.println(inputForce.toString());
        Vector3d newAcc = new Vector3d();
        if (gravityOn) {
            //newAcc.x = ((currentAcc.x + (frametime * 100 * inputForce.x)) / mass);
            //newAcc.y = ((currentAcc.y + (frametime * 100 * inputForce.y)) / mass);
            //newAcc.z = ((currentAcc.z + (frametime * 100 * inputForce.z)) / mass) - gravity;
            newAcc.x = ((frametime * scale * inputForce.x)) / mass;
            newAcc.y = ((frametime * scale * inputForce.y)) / mass;
            newAcc.z = (((frametime * scale * inputForce.z)) / mass) - gravity;

        } else {
            //newAcc.setX((currentAcc.x + (frametime * 100 * inputForce.x)) / mass);
            //newAcc.setY((currentAcc.y + (frametime * 100 * inputForce.y)) / mass);
            //newAcc.setZ((currentAcc.z + (frametime * 100 * inputForce.z)) / mass);
            newAcc.x = ((frametime * scale * inputForce.x)) / mass;
            newAcc.y = ((frametime * scale * inputForce.y)) / mass;
            newAcc.z = ((frametime * scale * inputForce.z)) / mass;

        }
        //Vector3d newAcc = new Vector3d();
        //System.out.printf("X: %.3f, Y: %.3f, Z: %.3f\n", currentAcc.getX(), currentAcc.getY(), currentAcc.getZ());
        //this.acc.x = (currentAcc.getX() + (frametime * inputForce.x)) / mass;
        //this.acc.y = (currentAcc.getY() + (frametime * inputForce.y)) / mass;
        //this.acc.z = (currentAcc.getZ() + (frametime * inputForce.z)) / mass;
        //this.acc.x = currentAcc.getX() + (frametime * inputForce.x) ;
        //this.acc.y = 420;
        //this.acc.z = 69;

        //System.out.printf("X: %.3f, Y: %.3f, Z: %.3f\n", acc.x, acc.y, acc.z);
        //System.out.println(this.acc.toString());
        return newAcc;
    }

    public Vector3d calcVel(Vector3d currentVel, Vector3d accVector, double frametime) {
        Vector3d newVel = new Vector3d();

        newVel.x = (currentVel.x + (frametime * accVector.x));
        newVel.y = (currentVel.y + (frametime * accVector.y));
        newVel.z = (currentVel.z + (frametime * accVector.z));

        //limitVelocity(newVel);

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
        limitVelocity(resultVel);
        airResistance(resultAcc, resultVel);
        thisMover.vel = resultVel;
        thisMover.pos = resultPos;

        //limitAcceleration(resultAcc);


        floorLock(resultVel, resultPos);
        //System.out.printf("Movement stuff\n");
        System.out.printf("Acc: %s\n", this.acc.toString());
        System.out.printf("Vel: %s\n", this.vel.toString());
        //System.out.printf("Pos: %s\n", this.pos.toString());
    }


    public void limitAcceleration(Vector3d acc) {
        double maxAccX = 5;
        double maxAccY = 5;
        double maxAccZ = 5;
        if (acc.getX() > maxAccX) {
            //this.getAcc().setX(0);
            acc.setX(maxAccX);
        } else if (acc.getX() < -maxAccX) {
            //this.getAcc().setX(0);
            acc.setX(-maxAccX);
        }
        if (acc.getY() > maxAccY) {
            //this.getAcc().setY(0);
            acc.setY(maxAccY);
        } else if (acc.getY() < -maxAccY) {
            //this.getAcc().setY(0);
            acc.setY(-maxAccY);
        }
        if (acc.getZ() > maxAccZ) {
            //this.getAcc().setZ(0);
            acc.setZ(maxAccZ);
        } else if (acc.getZ() < -maxAccZ) {
            //this.getAcc().setZ(0);
            acc.setZ(-maxAccZ);
        }
    }

    public void limitVelocity(Vector3d vel) {
        //double velX = getVelX();
        //double velY = getVelY();
        //double velZ = getVelZ();
        double maxVelX = 5;
        double maxVelY = 5;
        double maxVelZ = 5;
        if (vel.getX() > maxVelX) {
            this.getAcc().setX(0);
            vel.setX(maxVelX);
        } else if (vel.getX() < -maxVelX) {
            this.getAcc().setX(0);
            vel.setX(-maxVelX);
        }
        if (vel.getY() > maxVelY) {
            this.getAcc().setY(0);
            vel.setY(maxVelY);
        } else if (vel.getY() < -maxVelY) {
            this.getAcc().setY(0);
            vel.setY(-maxVelY);
        }
        if (vel.getZ() > maxVelZ) {
            this.getAcc().setZ(0);
            vel.setZ(maxVelZ);
        } else if (vel.getZ() < -maxVelZ) {
            this.getAcc().setZ(0);
            vel.setZ(-maxVelZ);
        }
    }

    public void airResistance(Vector3d acc, Vector3d vel) {
        //double velX = vel.getX();
        //double velY = vel.getY();
        //double velZ = vel.getZ();
        double velX = vel.getX();
        double velY = vel.getY();
        double velZ = vel.getZ();
        double newVel;
        //if (acc.getX() == 0) {
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
        //}
        //if (acc.getY() == 0) {
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
        //}
        //if (acc.getZ() == 0){
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
        //}
    }

    /*
    public void airResistance(Vector3d acc, Vector3d vel) {
        //double velX = vel.getX();
        //double velY = vel.getY();
        //double velZ = vel.getZ();
        double accX = acc.getX();
        double accY = acc.getY();
        double accZ = acc.getZ();
        double newAcc;
        //if (acc.getX() == 0) {
        if (accX > 0) {
            newAcc = accX - 0.1;
            if (accX < 0) {
                newAcc = 0;
            }
            acc.setX(newAcc);
        } else {
            newAcc = accX + 0.1;
            if (newAcc > 0) {
                newAcc = 0;
            }
            acc.setX(newAcc);
        }
        //}
        //if (acc.getY() == 0) {
        if (accY > 0) {
            newAcc = accY - 0.1;
            if (accY < 0) {
                newAcc = 0;
            }
            acc.setY(newAcc);
        } else {
            newAcc = accY + 0.1;
            if (newAcc > 0) {
                newAcc = 0;
            }
            acc.setY(newAcc);
        }
        //}
        //if (acc.getZ() == 0){
        if (accZ > 0) {
            newAcc = accZ - 0.1;
            if (accZ < 0) {
                newAcc = 0;
            }
            acc.setZ(newAcc);
        } else {
            newAcc = accZ + 0.1;
            if (newAcc > 0) {
                newAcc = 0;
            }
            acc.setZ(newAcc);
        }
        //}
    }*/

    public void floorLock(Vector3d vel, Vector3d pos) {
        if (pos.getZ() < 0 && vel.getZ() < 0) {
            vel.setZ(0);
            pos.setZ(0);
        }
    }

//    @Override
//    public Vector3d accept(Visitor visit) {
//        return visit.visit(visit, this, this.frametime);
//    }


//    Vector3d gravityVector = new Vector3d(0, 9.81, 0);
//
//    public Vector3d velocityCalc(Vector3d acc, Vector3d currentVel){
//        //This is wrong
//        return acc;
//    }

}
