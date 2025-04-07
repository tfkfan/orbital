package io.github.tfkfan.orbital.core.manager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseGatewayManager implements GatewayManager {
    protected final MatchmakerManager matchmakerManager;

    public BaseGatewayManager(MatchmakerManager matchmakerManager) {
        this.matchmakerManager = matchmakerManager;
    }
}
