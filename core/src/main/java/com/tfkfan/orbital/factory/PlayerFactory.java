package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.game.model.players.Player;
import com.tfkfan.orbital.game.room.GameRoom;
import com.tfkfan.orbital.session.UserSession;

@FunctionalInterface
public interface PlayerFactory<P extends Player, GR extends GameRoom> {
     P createPlayer(long nextId, GR gameRoom, UserSession userSession);
}
