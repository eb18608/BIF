package Java;

import javax.vecmath.Vector3d;

public class ModVisitor implements Visitor{

    @Override
    public Vector3d visit(ControlMod control, Movement movement, int frametime) {
        Vector3d resultantForce = movement.forceApplied(control.controlMod, movement.getAcc(),movement.getMass(), frametime);

        return resultantForce;
    }

    @Override
    public Vector3d visit(EnviroMod enviro, Movement movement, int frametime) {
        Vector3d resultantForce = movement.forceApplied(enviro.controlMod, movement.getAcc(), movement.getMass(), frametime);

        return resultantForce;
    }

    @Override
    public void visit(CollideMod collide, Movement movement, int frametime) {
        movement.setVel(collide.collideMod);


    }
}
