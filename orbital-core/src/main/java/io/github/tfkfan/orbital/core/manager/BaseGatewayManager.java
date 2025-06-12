package io.github.tfkfan.orbital.core.manager;


public abstract class BaseGatewayManager extends BaseManager implements GatewayManager {
    protected final MatchmakerManager matchmakerManager;

    public BaseGatewayManager(MatchmakerManager matchmakerManager) {
        this.matchmakerManager = matchmakerManager;
    }
}
