package com.tfkfan.vertx.factory;

import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.properties.RoomProperties;

import java.util.UUID;

public interface GameRoomFactory<GR extends GameRoom> {
    GR createGameRoom(String verticleId, UUID roomId, GameMap gameMap, GameManager<?, ?> gameManager, RoomProperties roomProperties);
}
