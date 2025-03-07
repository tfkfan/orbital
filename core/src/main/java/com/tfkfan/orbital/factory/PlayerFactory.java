package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.model.players.Player;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.session.UserSession;

@FunctionalInterface
public interface PlayerFactory {
    Player createPlayer(long nextId, GameRoom gameRoom, UserSession userSession);
}
