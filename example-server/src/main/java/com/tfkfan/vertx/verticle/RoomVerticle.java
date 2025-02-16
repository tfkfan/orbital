package com.tfkfan.vertx.verticle;

import com.tfkfan.vertx.BaseRoomVerticle;
import com.tfkfan.vertx.factory.GameRoomFactory;
import com.tfkfan.vertx.factory.PlayerFactory;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.DefaultPlayer;
import com.tfkfan.vertx.game.room.DefaultGameRoom;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.properties.RoomProperties;
import com.tfkfan.vertx.session.UserSession;
import io.vertx.core.Vertx;

import java.util.UUID;

public class RoomVerticle extends BaseRoomVerticle {
    @Override
    protected GameManager<DefaultPlayer, DefaultGameRoom, GameMap> createGameManager(String verticleId, Vertx vertx, RoomProperties properties) {
        return new GameManager<>(verticleId, vertx, properties,
                DefaultPlayer::new,
                GameMap::new,
                this::createGameRoom);
    }

    private DefaultGameRoom createGameRoom(String verticleId, UUID roomId, GameMap gameMap, RoomProperties roomProperties) {
        return new DefaultGameRoom(verticleId, roomId, gameMap, getGameManager(), roomProperties);
    }
}
