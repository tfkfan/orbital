package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.impl.MatchmakerManagerImpl;
import com.tfkfan.orbital.network.CompositeConnectionListener;
import com.tfkfan.orbital.network.RoomEventPublisher;

public interface MatchmakerManager extends RoomEventPublisher, CompositeConnectionListener {
    static MatchmakerManagerImpl create(RoomConfig roomConfig) {
        return new MatchmakerManagerImpl(roomConfig);
    }
}
