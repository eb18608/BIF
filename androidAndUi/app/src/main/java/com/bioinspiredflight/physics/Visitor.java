package com.bioinspiredflight.physics;

import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.GameSketch;



public interface Visitor {
    public void visit(ControlMod control);
    public void visit(EnviroMod enviro);
    public void visit(CollideMod collide);

    public void visit(ControlMod control, Movement movement);
    public void visit(EnviroMod enviro, Movement movement);
    public void visit(CollideMod collide, Movement movement);


    public void visit(ControlMod control, Movement movement, GameSketch sketch);
//    public void visit(EnviroMod enviro, Movement movement, GameSketch.buildingObject buildingObject, GameSketch.droneObject drone);
    public void visit(CollideMod collide, Movement movement, GameSketch sketch);
    public void visit(CollideMod collide, Movement movement, GameSketch sketch, GameObject gameObject);


}
