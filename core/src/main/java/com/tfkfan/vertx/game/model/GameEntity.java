package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.Initializable;
import com.tfkfan.vertx.game.Updatable;
import com.tfkfan.vertx.math.Vector;
import com.tfkfan.vertx.network.pack.init.IInitPackProvider;
import com.tfkfan.vertx.network.pack.update.IUpdatePackProvider;

public interface GameEntity<I> extends Updatable, Initializable, IUpdatePackProvider, IInitPackProvider {
    I getId();

    boolean isMoving();

    boolean isAlive();

    Vector getPosition();

    Vector getVelocity();

    Vector getAcceleration();
}
