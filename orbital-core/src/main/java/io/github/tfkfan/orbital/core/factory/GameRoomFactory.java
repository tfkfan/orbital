package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.state.GameState;
import io.vertx.core.Vertx;

import java.util.UUID;

public interface GameRoomFactory<R extends GameRoom, S extends GameState> {
    R createGameRoom(
            Vertx vertx,
            String verticleId,
            UUID roomId,
            RoomType roomType,
            S gameState,
            GameManager gameManager,
            RoomConfig roomConfig);
}
