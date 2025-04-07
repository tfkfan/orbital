package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.impl.MatchmakerManagerImpl;
import io.github.tfkfan.orbital.core.network.RoomEventPublisher;
import io.github.tfkfan.orbital.core.network.GatewaySessionListener;

public interface MatchmakerManager extends RoomEventPublisher, GatewaySessionListener {
    static MatchmakerManagerImpl create(RoomConfig roomConfig) {
        return new MatchmakerManagerImpl(roomConfig);
    }
}
