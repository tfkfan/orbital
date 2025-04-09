package io.github.tfkfan.orbital.core.manager.impl;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.BaseMatchmakerManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchmakerManagerImpl extends BaseMatchmakerManager {
    public MatchmakerManagerImpl(RoomConfig roomConfig) {
        super(roomConfig);
    }
}
