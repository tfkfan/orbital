package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.model.players.DefaultPlayer;
import com.tfkfan.orbital.game.room.DefaultGameRoom;
import com.tfkfan.orbital.properties.RoomProperties;
import io.vertx.core.Vertx;

public class DefaultGameManager extends GameManager<DefaultPlayer, DefaultGameRoom> {
    public DefaultGameManager(String verticleId, Vertx vertx, RoomProperties roomProperties) {
        super(verticleId, vertx, roomProperties,
                DefaultPlayer::new,
                GameMap::new,
                DefaultGameRoom::new);
    }
}
