package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.manager.impl.GameManagerImpl;
import io.github.tfkfan.orbital.core.model.DefaultPlayer;
import io.github.tfkfan.orbital.core.room.DefaultGameRoom;
import io.github.tfkfan.orbital.core.state.impl.BaseGameState;
import io.github.tfkfan.resources.GeometryResources;
import io.vertx.core.Vertx;

public class DefaultGameManager extends GameManagerImpl<DefaultGameRoom, BaseGameState> {
    public static GameManagerFactory factory(GeometryResources geometryResources, RoomConfig roomConfig) {
        return (vId) -> new DefaultGameManager(vId, geometryResources, roomConfig);
    }

    private final GeometryResources geometryResources;

    public DefaultGameManager(String verticleId, GeometryResources geometryResources, RoomConfig roomConfig) {
        super(verticleId, Vertx.currentContext().owner(), roomConfig,
                DefaultPlayer::new,
                BaseGameState::new,
                DefaultGameRoom::new);
        this.geometryResources = geometryResources;
    }
}
