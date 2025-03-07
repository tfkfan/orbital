package com.tfkfan.orbital.state;

import com.tfkfan.orbital.model.players.Player;

import java.util.Collection;

public interface GameState {
    Player getPlayerById(long id);

    Collection<Player> getPlayers();

    void addPlayer(Player player);

    void removePlayer(Player player);

    long nextPlayerId();

    long alivePlayers();
}
