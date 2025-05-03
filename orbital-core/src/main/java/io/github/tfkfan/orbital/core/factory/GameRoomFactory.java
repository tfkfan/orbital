package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public interface GameRoomFactory<R extends GameRoom, S extends GameState> {
    R createGameRoom(String verticleId, UUID roomId,
                     S gameState,
                     GameManager gameManager,
                     RoomConfig roomConfig);
}
