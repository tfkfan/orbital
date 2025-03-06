package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.network.SessionListener;
import com.tfkfan.orbital.network.VerticleListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseGatewayManager implements GatewayManager {
    protected final MatchmakerManager matchmakerManager;

    public BaseGatewayManager(MatchmakerManager matchmakerManager) {
        this.matchmakerManager = matchmakerManager;
    }

    @Override
    public VerticleListener getVerticleListener() {
        return matchmakerManager.getVerticleListener();
    }

    @Override
    public SessionListener getSessionListener() {
        return matchmakerManager.getSessionListener();
    }
}
