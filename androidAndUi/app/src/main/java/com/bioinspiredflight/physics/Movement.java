package com.bioinspiredflight.physics;
import javax.vecmath.Vector3d;
/*Movement is my testing class for the visitor functions capabilities
Movement is meant to simulate any object in the game that will be affected by any form of movement interaction
If a player, then would have all the interactions (control from player input, environment obstacles or even wall
collisions*/


public class Movement{
    private double mass;
    private boolean gravityOn;
    private Vector3d pos;
    private Vector3d vel;
    private Vector3d acc;
    private final double framerate = 30;
    public final double frametime = 1/ framerate;

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
    public double getPosX() {return pos.x; }
    public double getPosZ() {return pos.z; }
    public double getPosY() {return pos.y; }

    //Get the individual components of the velocity vector to do math on them
    public double getVelX(){return vel.x;  }
    public double getVelZ(){return vel.z;  }
    public double getVelY(){return vel.y;  }

    //Get the individual components of the acceleration vector to do math on them
    public double getAccX(){return acc.x;  }
    public double getAccZ(){return acc.z;  }
    public double getAccY(){return acc.y;  }

    //Set the values for pos, vel, acc
    public Vector3d getAcc() {return acc;  }

    public Vector3d getVel() {return vel;  }

    public Vector3d getPos() {return pos;  }

    public void setPos(Vector3d pos) {this.pos = pos;  }

    public void setVel(Vector3d vel) {this.vel = vel;  }

    public void setAcc(Vector3d acc) {this.acc = acc;  }

    public void setMass(int m) {this.mass = m;  }

    public double getMass() {return mass;  }


    public Vector3d forceApplied(Vector3d currentAcc,Vector3d inputForce, double mass, double frametime) {
        double gravity = 9.81;
        //System.out.println(inputForce.toString());
        Vector3d newAcc = new Vector3d();
        if (gravityOn) {
            newAcc.x = ((currentAcc.x + (frametime * 100*inputForce.x)) / mass);
            newAcc.y = ((currentAcc.y + (frametime * 100*inputForce.y)) / mass);
            newAcc.z = ((currentAcc.z + (frametime * 100*inputForce.z)) / mass) - gravity;

        } else {
            newAcc.setX((currentAcc.x + (frametime * 100*inputForce.x)) / mass);
            newAcc.setY((currentAcc.y + (frametime * 100*inputForce.y)) / mass);
            newAcc.setZ((currentAcc.z + (frametime * 100*inputForce.z)) / mass);

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

    public Vector3d calcVel(Vector3d currentVel, Vector3d accVector, double frametime){
        Vector3d newVel = new Vector3d();

        newVel.x = (currentVel.x + (frametime*accVector.x));
        newVel.y = (currentVel.y + (frametime*accVector.y));
        newVel.z = (currentVel.z + (frametime*accVector.z));

        return newVel;

    }

    public Vector3d calcPos(Vector3d currentPos, Vector3d velVector, double frametime){
        Vector3d newPos = new Vector3d();

        newPos.x = (currentPos.x +(frametime*velVector.x));
        newPos.y = (currentPos.y +(frametime*velVector.y));
        newPos.z = (currentPos.z +(frametime*velVector.z));

        return newPos;
    }

    public void updateMover(Vector3d resultAcc, Vector3d resultVel, Vector3d resultPos, Movement thisMover){
        thisMover.acc = resultAcc;
        thisMover.vel = resultVel;
        thisMover.pos = resultPos;
        System.out.printf("\nAcc: %s\n", this.acc.toString());
        System.out.printf("Vel: %s\n", this.vel.toString());
        System.out.printf("Pos: %s\n", this.pos.toString());
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
