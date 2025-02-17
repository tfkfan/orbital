package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.Initializable;
import com.tfkfan.vertx.game.Updatable;
import com.tfkfan.vertx.math.Vector2D;
import com.tfkfan.vertx.network.pack.init.IInitPackProvider;
import com.tfkfan.vertx.network.pack.update.IUpdatePackProvider;

public interface GameEntity<I> extends Updatable, Initializable, IUpdatePackProvider, IInitPackProvider {
    I getId();

    boolean isMoving();

    boolean isAlive();

    void setPosition(Vector2D position);

    Vector2D getPosition();

    void setVelocity(Vector2D position);

    Vector2D getVelocity();

    void setAcceleration(Vector2D position);

    Vector2D getAcceleration();
}
