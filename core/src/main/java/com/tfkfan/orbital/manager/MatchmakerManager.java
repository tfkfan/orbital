package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.manager.impl.MatchmakerManagerImpl;
import com.tfkfan.orbital.network.CompositeConnectionListener;
import com.tfkfan.orbital.network.RoomEventPublisher;

public interface MatchmakerManager extends RoomEventPublisher, CompositeConnectionListener {
    default MatchmakerManagerImpl create(Integer roomMaxPlayers) {
        return new MatchmakerManagerImpl(roomMaxPlayers);
    }
}
