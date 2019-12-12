package Java;

import javax.vecmath.Vector3d;

public class EnviroMod implements ModVisitable{


        Vector3d environMod;

        EnviroMod(Vector3d moveVector){
            this.environMod = moveVector;
        }

    @Override
    public void accept(Visitor visit) {
        visit.visit(this);
    }
}


