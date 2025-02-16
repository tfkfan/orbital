package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.math.Vector;

public interface GameEntity<I> {
    I getId();

    boolean isMoving();

    boolean isAlive();

    Vector getPosition();

    Vector getVelocity();

    Vector getAcceleration();
}
