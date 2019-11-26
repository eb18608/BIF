package Java;

import javax.vecmath.Vector3d;

public interface Visitor {
    public Vector3d visit(ControlMod control, Movement movement, int frametime);
    public Vector3d visit(EnviroMod enviro, Movement movement, int frametime);
    public void visit(CollideMod collide, Movement movement, int frametime);


}
