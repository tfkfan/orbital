package io.github.tfkfan.orbital.core.state;

import io.github.tfkfan.orbital.core.model.players.Player;

import java.util.Collection;

public interface GameState {

    <T extends Player> T getPlayerById(long id);

    <T extends Player> Collection<T> getPlayers();

    <T extends Player> void addPlayer(T player);

    <T extends Player> void removePlayer(T player);

    long nextPlayerId();

    long alivePlayers();

    long deadPlayers();
}
