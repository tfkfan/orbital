package com.tfkfan.orbital.verticle.impl;


import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.configuration.props.ServerConfig;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.manager.WebSocketManager;
import com.tfkfan.orbital.manager.impl.WebSocketManagerImpl;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketGatewayVerticle extends GatewayVerticle {
    final WebSocketManager webSocketManager;

    public WebsocketGatewayVerticle(ServerConfig serverConfig, RoomConfig roomConfig) {
        super(serverConfig, new WebSocketManagerImpl(MatchmakerManager.create(roomConfig)));
        webSocketManager = (WebSocketManager) gatewayManager;
    }

    public WebsocketGatewayVerticle(ServerConfig serverConfig, WebSocketManager webSocketManager) {
        super(serverConfig, webSocketManager);
        this.webSocketManager = webSocketManager;
    }

    @Override
    void customize(HttpServer server) {
        server.webSocketHandler(webSocketManager);
    }
}
