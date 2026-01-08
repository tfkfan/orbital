package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.manager.impl.GameManagerImpl;
import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.MotionType;
import io.github.tfkfan.orbital.core.model.players.Player2D;
import io.github.tfkfan.orbital.core.room.DefaultGameRoom;
import io.github.tfkfan.orbital.core.state.impl.BaseGameState;
import io.github.tfkfan.resources.GeometryResources;
import io.vertx.core.Vertx;

import java.util.function.Function;

public class DefaultGameManager extends GameManagerImpl<DefaultGameRoom, BaseGameState> {
    private static Double ABS_PLAYER_SPEED = 10.0;

    public static Function<OrbitalConfig, GameManagerFactory> factory() {
        return config -> vId -> new DefaultGameManager(vId, new GeometryResources().load(), config.getRoom());
    }

    public DefaultGameManager(String verticleId, GeometryResources geometryResources, RoomConfig roomConfig) {
        super(verticleId, Vertx.currentContext().owner(), roomConfig,
                (nextId, gameRoom, userSession, inputData) ->
                        new Player2D(nextId,
                                gameRoom,
                                userSession,
                                MotionType.STATE,
                                new Vector2D(),
                                new Vector2D(ABS_PLAYER_SPEED, ABS_PLAYER_SPEED)
                        ),
                BaseGameState::new,
                DefaultGameRoom::new);
    }
}
