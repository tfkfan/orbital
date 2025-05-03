package io.github.tfkfan.orbital.core.verticle.impl;


import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import io.github.tfkfan.orbital.core.manager.MatchmakerManager;
import io.github.tfkfan.orbital.core.manager.WebSocketManager;
import io.github.tfkfan.orbital.core.manager.impl.WebSocketManagerImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketGatewayVerticle extends GatewayVerticle {
    final WebSocketManager webSocketManager;

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig) {
        super(serverConfig, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)));
        webSocketManager = (WebSocketManager) gatewayManager;
        websocket();
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, MatchmakerManager matchmakerManager) {
        super(serverConfig, new WebSocketManagerImpl(matchmakerManager));
        webSocketManager = (WebSocketManager) gatewayManager;
        websocket();
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, WebSocketManager webSocketManager) {
        super(serverConfig, webSocketManager);
        this.webSocketManager = webSocketManager;
        websocket();
    }

    private void websocket(){
       withServerCustomizer(server-> server.webSocketHandler(webSocketManager));
    }
}
