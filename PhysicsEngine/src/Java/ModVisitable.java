package Java;

import javax.vecmath.Vector3d;

public interface ModVisitable {
    public Vector3d accept(Visitor visit);
}
