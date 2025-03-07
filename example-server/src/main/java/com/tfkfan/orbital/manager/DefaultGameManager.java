package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.impl.GameManagerImpl;
import com.tfkfan.orbital.players.DefaultPlayer;
import com.tfkfan.orbital.room.DefaultGameRoom;
import com.tfkfan.orbital.state.impl.BaseGameState;
import io.vertx.core.Vertx;

import java.util.function.Function;

public class DefaultGameManager extends GameManagerImpl {
    public static Function<String, GameManager> gameManagerFactory(RoomConfig roomConfig) {
        return (vId) -> new DefaultGameManager(vId, roomConfig);
    }

    public DefaultGameManager(String verticleId, RoomConfig roomConfig) {
        super(verticleId, Vertx.currentContext().owner(), roomConfig,
                DefaultPlayer::new,
                BaseGameState::new,
                DefaultGameRoom::new);
    }
}
