package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.room.GameRoom;
import com.tfkfan.orbital.manager.GameManager;

import java.util.UUID;

public interface GameRoomFactory<GR extends GameRoom, RP> {
    GR createGameRoom(String verticleId, UUID roomId, GameMap gameMap, GameManager gameManager, RP roomProperties);
}
