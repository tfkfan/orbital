package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public interface GameRoomFactory {
    GameRoom createGameRoom(String verticleId, UUID roomId, GameState gameState, GameManager gameManager, RoomConfig roomConfig);
}
