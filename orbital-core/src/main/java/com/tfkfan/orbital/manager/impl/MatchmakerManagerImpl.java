package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.BaseMatchmakerManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchmakerManagerImpl extends BaseMatchmakerManager {
    public MatchmakerManagerImpl(RoomConfig roomConfig) {
        super(roomConfig);
    }
}
