package com.tfkfan.orbital.manager;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

public interface WebSocketManager extends GatewayManager, Handler<ServerWebSocket> {
}
