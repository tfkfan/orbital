package io.github.tfkfan.orbital.core.verticle.impl;


import io.github.tfkfan.orbital.core.ConfigurationContext;
import io.github.tfkfan.orbital.core.OrbitalClusterManager;
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

    public WebsocketGatewayVerticle(ConfigurationContext configurationContext) {
        this(configurationContext.getConfig().getServer(), configurationContext.getConfig().getRoom(), null);
    }

    public WebsocketGatewayVerticle(ConfigurationContext configurationContext, DeploymentOptions deploymentOptions, OrbitalClusterManager clusterManager) {
        this(configurationContext.getConfig().getServer(), configurationContext.getConfig().getRoom(), clusterManager, deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig, OrbitalClusterManager clusterManager) {
        this(serverConfig, clusterManager, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)), null);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig, OrbitalClusterManager clusterManager, DeploymentOptions deploymentOptions) {
        this(serverConfig, clusterManager, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)), deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, OrbitalClusterManager clusterManager, MatchmakerManager matchmakerManager, DeploymentOptions deploymentOptions) {
        this(serverConfig, clusterManager, new WebSocketManagerImpl(matchmakerManager), deploymentOptions);
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, OrbitalClusterManager clusterManager, WebSocketManager webSocketManager, DeploymentOptions deploymentOptions) {
        super(clusterManager, serverConfig, webSocketManager, deploymentOptions);
        this.webSocketManager = webSocketManager;
        websocket();
    }

    private void websocket() {
        withServerCustomizer(server -> server.webSocketHandler(webSocketManager));
    }
}
