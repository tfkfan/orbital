package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.impl.GameManagerImpl;
import io.github.tfkfan.orbital.core.model.DefaultPlayer;
import io.github.tfkfan.orbital.core.room.DefaultGameRoom;
import io.github.tfkfan.orbital.core.state.impl.BaseGameState;
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
