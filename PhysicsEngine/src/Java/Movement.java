package Java;
import javax.vecmath.Vector3d;
/*Movement is my testing class for the visitor functions capabilities
Movement is meant to simulate any object in the game that will be affected by any form of movement interaction
If a player, then would have all the interactions (control from player input, environment obstacles or even wall
collisions*/


public class Movement implements Visitor{
    private int mass;
    private boolean gravityOn;
    private Vector3d pos;
    private Vector3d vel;
    private Vector3d acc;
    private int framerate = 30;
    private double frametime = 1/ framerate;

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

    public int getMass() {return mass;  }


    public Vector3d forceApplied(Vector3d currentAcc,Vector3d inputForce, int mass, double frametime){

        Vector3d newAcc = new Vector3d();

        newAcc.x = ((currentAcc.x + (frametime*inputForce.x))/mass);
        newAcc.y = ((currentAcc.y + (frametime*inputForce.y))/mass);
        newAcc.z = ((currentAcc.z + (frametime*inputForce.z))/mass);

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
    }

    @Override
    public void visit(ControlMod control) {
        Vector3d resultantAcc = forceApplied(this.acc, control.controlMod, this.mass, this.frametime);
        Vector3d resultantVel = calcVel(this.vel, resultantAcc, this.frametime);
        Vector3d resultantPos = calcPos(this.pos, resultantVel, this.frametime);

        updateMover(resultantAcc, resultantVel, resultantPos, this);
    }


    @Override
    public void visit(EnviroMod enviro) {
        Vector3d resultantAcc = forceApplied(this.acc, enviro.environMod, this.mass, this.frametime);
        Vector3d resultantVel = calcVel(this.vel, resultantAcc, this.frametime);
        Vector3d resultantPos = calcPos(this.pos, resultantVel, this.frametime);

        updateMover(resultantAcc, resultantVel, resultantPos, this);
    }

    @Override
    public void visit(CollideMod collide) {
        Vector3d resultantAcc = this.acc;
        Vector3d resultantVel = collide.collideMod;
        Vector3d resultantPos = this.pos;

        updateMover(resultantAcc, resultantVel, resultantPos, this);
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
