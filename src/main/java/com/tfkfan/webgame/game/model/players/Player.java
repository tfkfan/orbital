package com.tfkfan.webgame.game.model.players;

import com.tfkfan.webgame.game.model.Entity;
import com.tfkfan.webgame.math.Vector;

public interface Player extends Entity<Long> {
    Vector getPosition();
    void setPosition(Vector position);
}
