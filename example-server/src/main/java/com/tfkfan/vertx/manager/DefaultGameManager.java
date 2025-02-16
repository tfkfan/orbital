package com.tfkfan.vertx.manager;

import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.DefaultPlayer;
import com.tfkfan.vertx.game.room.DefaultGameRoom;
import com.tfkfan.vertx.properties.RoomProperties;
import io.vertx.core.Vertx;

public class DefaultGameManager extends GameManager<DefaultPlayer, DefaultGameRoom> {
    public DefaultGameManager(String verticleId, Vertx vertx, RoomProperties roomProperties) {
        super(verticleId, vertx, roomProperties,
                DefaultPlayer::new,
                GameMap::new,
                DefaultGameRoom::new);
    }
}
