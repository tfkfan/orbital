package com.tfkfan.vertx.factory;

import com.tfkfan.vertx.game.model.players.Player;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.session.UserSession;

@FunctionalInterface
public interface PlayerFactory<P extends Player, GR extends GameRoom> {
     P createPlayer(long nextId, GR gameRoom, UserSession userSession);
}
