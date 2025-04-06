package com.tfkfan.orbital.model;

import com.tfkfan.orbital.math.Vector;

public interface DynamicEntity<I, V extends Vector<V>> extends GameEntity<I>{

    void setPosition(V position);

    V getPosition();

    void setVelocity(V position);

    V getVelocity();

    void setAcceleration(V position);

    V getAcceleration();
}
