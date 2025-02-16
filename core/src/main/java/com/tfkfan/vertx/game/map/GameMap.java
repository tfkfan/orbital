package com.tfkfan.vertx.game.map;

import com.tfkfan.vertx.game.model.GameEntity;
import com.tfkfan.vertx.game.model.players.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Baltser Artem tfkfan
 */
public class GameMap {
    private Map<Long, Player> players = new HashMap<>();

    public GameMap() {
    }

    public Player getPlayerById(long id) {
        return players.get(id);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void removePlayer(Player player) {
        players.remove(player.getId());
    }

    public long nextPlayerId() {
        return (long) (players.size() + 1);
    }

    public long alivePlayers() {
        return players.values().stream().filter(GameEntity::isAlive).count();
    }
}
