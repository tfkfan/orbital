package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.ConfigurationContext;
import io.github.tfkfan.orbital.core.manager.GameRoomManager;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public interface GameRoomFactory<R extends GameRoom, S extends GameState> {
    R createGameRoom(String verticleId,
                     UUID roomId,
                     RoomType roomType,
                     S gameState,
                     GameRoomManager gameRoomManager,
                     ConfigurationContext configurationContext);
}
