package com.tfkfan.orbital.game.model;

import com.tfkfan.orbital.game.Initializable;
import com.tfkfan.orbital.game.Updatable;
import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.network.pack.init.IInitPackProvider;
import com.tfkfan.orbital.network.pack.update.IUpdatePackProvider;

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
