package Java;

import javax.vecmath.Vector3d;

public class CollideMod implements ModVisitable {

    Vector3d collideMod;

    CollideMod(Vector3d moveVector){
        this.collideMod = moveVector;
    }


    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }
}
