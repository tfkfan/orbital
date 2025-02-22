package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.model.players.DefaultPlayer;
import com.tfkfan.orbital.game.room.DefaultGameRoom;
import com.tfkfan.orbital.manager.impl.GameManagerImpl;
import com.tfkfan.orbital.properties.RoomProperties;
import io.vertx.core.Vertx;

public class DefaultGameManager extends GameManagerImpl<DefaultPlayer, DefaultGameRoom, RoomProperties> {
    public static DefaultGameManager create(String verticleId, RoomProperties roomProperties) {
        return new DefaultGameManager(verticleId, roomProperties);
    }

    public DefaultGameManager(String verticleId, RoomProperties roomProperties) {
        super(verticleId, Vertx.currentContext().owner(), roomProperties,
                DefaultPlayer::new,
                GameMap::new,
                DefaultGameRoom::new);
    }
}
