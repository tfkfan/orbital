package com.tfkfan.vertx.game.model.players;

import com.tfkfan.vertx.game.model.GameEntity;
import com.tfkfan.vertx.math.Vector;

public interface Player extends GameEntity<Long> {
    Vector getPosition();
    void setPosition(Vector position);
}
