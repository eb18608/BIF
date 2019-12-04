package com.bioinspiredflight.physics;

public interface ModVisitable {
    public void accept(Visitor visit);
    public void accept(Visitor visit, Movement movement);
}