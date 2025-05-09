package io.github.tfkfan.orbital.core.state.impl;

import io.github.tfkfan.orbital.core.model.GameEntity;
import io.github.tfkfan.orbital.core.model.players.Player;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Baltser Artem tfkfan
 */
public class BaseGameState implements GameState {
    private final Map<Long, Player> players = new HashMap<>();

    public BaseGameState() {
    }

    @Override
    public Player getPlayerById(long id) {
        return players.get(id);
    }

    @Override
    public Collection<Player> getPlayers() {
        return players.values();
    }

    @Override
    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    @Override
    public void removePlayer(Player player) {
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
