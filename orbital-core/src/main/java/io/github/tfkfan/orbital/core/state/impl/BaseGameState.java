package io.github.tfkfan.orbital.core.state.impl;

import io.github.tfkfan.orbital.core.model.GameEntity;
import io.github.tfkfan.orbital.core.model.players.Player;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Baltser Artem tfkfan
 */
@SuppressWarnings("unchecked")
public class BaseGameState implements GameState {
    private final Map<Long, Player> players = new HashMap<>();

    public BaseGameState() {
    }

    @Override
    public <T extends Player> T getPlayerById(long id) {
        return (T) players.get(id);
    }

    @Override
    public <T extends Player> Collection<T> getPlayers() {
        return (Collection<T>) players.values();
    }

    @Override
    public <T extends Player> void addPlayer(T player) {
        players.put(player.getId(), player);
    }

    @Override
    public <T extends Player> void removePlayer(T player) {
        players.remove(player.getId());
    }

    @Override
    public long nextPlayerId() {
        return players.size() + 1;
    }

    @Override
    public long alivePlayers() {
        return players.values().stream().filter(GameEntity::isAlive).count();
    }

    @Override
    public long deadPlayers() {
        return players.values().stream().filter(GameEntity::isDead).count();
    }
}
