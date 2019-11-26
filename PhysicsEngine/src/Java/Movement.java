package Java;
import javax.vecmath.Vector3d;

public abstract class Movement {
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


    public Vector3d forceApplied(Vector3d currentAcc,Vector3d inputForce, int mass, int frametime){
        this.acc.x = ((currentAcc.x + (frametime*inputForce.x))/mass);
        this.acc.y = ((currentAcc.y + (frametime*inputForce.y))/mass);
        this.acc.z = ((currentAcc.z + (frametime*inputForce.z))/mass);

        return this.acc;
    }

    public Vector3d calcVel(Vector3d currentVel, Vector3d accVector, int mass, int frametime){
        this.vel.x = ((currentVel.x + (frametime*accVector.x)));
        this.vel.y = ((currentVel.y + (frametime*accVector.y)));
        this.vel.z = ((currentVel.z + (frametime*accVector.z)));

        return this.vel;

    }


//    Vector3d gravityVector = new Vector3d(0, 9.81, 0);
//
//    public Vector3d velocityCalc(Vector3d acc, Vector3d currentVel){
//        //This is wrong
//        return acc;
//    }
}
