package Java;
import javax.vecmath.Vector3d;

public abstract class Movement {
    private Vector3d pos;
    private Vector3d vel;
    private Vector3d acc;


    //Get the individual components of the position vector to do math on them
    public double getPosX(){
        return pos.x;
    }
    public double getPosZ(){
        return pos.z;
    }
    public double getPosY(){
        return pos.y;
    }

    //Get the individual components of the velocity vector to do math on them
    public double getVelX(){
        return vel.x;
    }
    public double getVelZ(){
        return vel.z;
    }
    public double getVelY(){
        return vel.y;
    }

    //Get the individual components of the acceleration vector to do math on them
    public double getAccX(){
        return acc.x;
    }
    public double getAccZ(){
        return acc.z;
    }
    public double getAccY(){
        return acc.y;
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


    public Vector3d forceApplied(Vector3d force, int mass){
        this.acc.x = force.x / mass;
        this.acc.y = force.y / mass;
        this.acc.z = force.z / mass;

        return this.acc;
    }
    Vector3d gravityVector = new Vector3d(0, 9.81, 0);

    public Vector3d velocityCalc(Vector3d acc, Vector3d currentVel){
        //This is wrong
        return acc;
    }
}
