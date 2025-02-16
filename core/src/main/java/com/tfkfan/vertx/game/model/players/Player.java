package com.tfkfan.vertx.game.model.players;

import com.tfkfan.vertx.game.model.GameEntity;
import com.tfkfan.vertx.math.Vector;
import com.tfkfan.vertx.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.vertx.session.UserSession;

public interface Player extends GameEntity<Long>, IPrivateUpdatePackProvider {
    UserSession getUserSession();
    Vector getPosition();
    void setPosition(Vector position);
}
