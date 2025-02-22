package com.tfkfan.orbital.verticle.impl;


import com.tfkfan.orbital.manager.WebSocketManager;
import com.tfkfan.orbital.manager.impl.WebSocketManagerImpl;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketGatewayVerticle extends GatewayVerticle {
    final WebSocketManager webSocketManager;

    public WebsocketGatewayVerticle(int port, WebSocketManager webSocketManager) {
        super(port, webSocketManager);
        this.webSocketManager = webSocketManager;
    }

    @Override
    void customize(HttpServer server) {
        server.webSocketHandler(webSocketManager);
    }
}
