package io.github.tfkfan.orbital.core.model;

import org.locationtech.spatial4j.shape.Point;

import java.io.Serializable;

public interface DynamicEntity<I extends Serializable> extends GameEntity<I>{

    void setPosition(Point position);

    Point getPosition();

    void setVelocity(Point position);

    Point getVelocity();

    void setAcceleration(Point position);

    Point getAcceleration();
}
