package com.tfkfan.vertx.game.model.players;

import com.tfkfan.vertx.game.model.Entity;
import com.tfkfan.vertx.math.Vector;

public interface Player extends Entity<Long> {
    Vector getPosition();
    void setPosition(Vector position);
}
