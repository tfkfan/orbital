package com.tfkfan.webgame.game.map;

import com.tfkfan.webgame.game.model.BaseGameEntity;
import com.tfkfan.webgame.game.model.players.DefaultPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Baltser Artem tfkfan
 */

public class GameMap {
    private Map<Long, DefaultPlayer> players = new HashMap<>();

    public GameMap() {
    }

    public DefaultPlayer getPlayerById(long id) {
        return players.get(id);
    }

    public Collection<DefaultPlayer> getPlayers() {
        return players.values();
    }

    public void addPlayer(DefaultPlayer player) {
        players.put(player.getId(), player);
    }

    public void removePlayer(DefaultPlayer player) {
        players.remove(player.getId());
    }

    public long nextPlayerId() {
        return (long) (players.size() + 1);
    }

    public long alivePlayers() {
        return players.values().stream().filter(BaseGameEntity::isAlive).count();
    }
}
