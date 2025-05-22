package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.math.Vector;

import java.io.Serializable;

public interface DynamicEntity<I extends Serializable, V extends Vector<V>> extends GameEntity<I>{

    void setPosition(V position);

    V getPosition();

    void setVelocity(V position);

    V getVelocity();

    void setAcceleration(V position);

    V getAcceleration();
}
