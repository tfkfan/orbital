package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.model.players.Player;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;

@FunctionalInterface
public interface PlayerFactory {
    Player createPlayer(long nextId, GameRoom gameRoom, PlayerSession userSession);
}
