package com.tfkfan.vertx.factory;

import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.properties.RoomProperties;

import java.util.UUID;

public interface GameRoomFactory<GM extends GameMap, GR extends GameRoom> {
    GR createGameRoom(String verticleId, UUID roomId, GM gameMap, RoomProperties roomProperties);
}
