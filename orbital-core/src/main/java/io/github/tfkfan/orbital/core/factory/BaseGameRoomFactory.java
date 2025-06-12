package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public class BaseGameRoomFactory<R extends GameRoom, S extends GameState> implements GameRoomFactory<R, S> {

    @Override
    public R createGameRoom(String verticleId, UUID roomId, RoomType roomType, S gameState, GameManager gameManager, RoomConfig roomConfig) {
        return null;
    }
}
