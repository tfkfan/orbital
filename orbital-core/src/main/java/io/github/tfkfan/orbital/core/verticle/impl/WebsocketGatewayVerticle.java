package io.github.tfkfan.orbital.core.verticle.impl;


import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import io.github.tfkfan.orbital.core.manager.MatchmakerManager;
import io.github.tfkfan.orbital.core.manager.WebSocketManager;
import io.github.tfkfan.orbital.core.manager.impl.WebSocketManagerImpl;
import io.vertx.core.DeploymentOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketGatewayVerticle extends GatewayVerticle {
    final WebSocketManager webSocketManager;

    public WebsocketGatewayVerticle(OrbitalConfig config) {
        this(config.getServer(), config.getRoom(), null);
    }

    public WebsocketGatewayVerticle(OrbitalConfig config, DeploymentOptions deploymentOptions) {
        this(config.getServer(), config.getRoom(), deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig) {
        this(serverConfig, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)), null);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig, DeploymentOptions deploymentOptions) {
        this(serverConfig, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)), deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, MatchmakerManager matchmakerManager, DeploymentOptions deploymentOptions) {
        this(serverConfig, new WebSocketManagerImpl(matchmakerManager), deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, WebSocketManager webSocketManager, DeploymentOptions deploymentOptions) {
        super(serverConfig, webSocketManager, deploymentOptions);
        this.webSocketManager = webSocketManager;
        websocket();
    }

    private void websocket() {
        withServerCustomizer(server -> server.webSocketHandler(webSocketManager));
    }
}
