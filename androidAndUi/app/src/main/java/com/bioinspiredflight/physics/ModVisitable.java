package com.bioinspiredflight.physics;

import com.bioinspiredflight.GameSketch;


public interface ModVisitable {
    public void accept(Visitor visit);
    public void accept(Visitor visit, Movement movement);
//    public void accept(Visitor visit, Movement movement, PVector lastNonCollision);

    public void accept(Visitor visit, Movement movement, GameSketch sketch);
}
