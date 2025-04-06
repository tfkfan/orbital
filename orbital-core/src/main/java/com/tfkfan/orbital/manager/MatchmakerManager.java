package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.impl.MatchmakerManagerImpl;
import com.tfkfan.orbital.network.RoomEventPublisher;
import com.tfkfan.orbital.network.GatewaySessionListener;

public interface MatchmakerManager extends RoomEventPublisher, GatewaySessionListener {
    static MatchmakerManagerImpl create(RoomConfig roomConfig) {
        return new MatchmakerManagerImpl(roomConfig);
    }
}
