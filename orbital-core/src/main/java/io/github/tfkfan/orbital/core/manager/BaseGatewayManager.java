package io.github.tfkfan.orbital.core.manager;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseGatewayManager extends BaseManager implements GatewayManager {
    protected final MatchmakerManager matchmakerManager;

    protected BaseGatewayManager(Vertx vertx,MatchmakerManager matchmakerManager) {
        super(vertx);
        this.matchmakerManager = matchmakerManager;
    }
}
