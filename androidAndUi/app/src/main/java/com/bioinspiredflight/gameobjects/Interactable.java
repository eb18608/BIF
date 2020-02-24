package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.physics.CollideMod;
import com.bioinspiredflight.physics.Movement;

public interface Interactable {

    public void collide(CollideMod collideMod, Movement movement, GameSketch sketch);

    public boolean isDrone();

    public void draw();

}
