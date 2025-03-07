package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.state.GameState;

import java.util.UUID;

public interface GameRoomFactory {
    GameRoom createGameRoom(String verticleId, UUID roomId, GameState gameState, GameManager gameManager, RoomConfig roomConfig);
}
