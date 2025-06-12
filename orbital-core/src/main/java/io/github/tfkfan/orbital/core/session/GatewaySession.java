package io.github.tfkfan.orbital.core.session;

import io.vertx.core.http.ServerWebSocket;

import java.util.UUID;

public class GatewaySession extends Session {
    private final ServerWebSocket webSocket;
    private UUID roomKey;

    public GatewaySession(boolean admin, ServerWebSocket webSocket) {
        super(admin);
        this.webSocket = webSocket;
    }

    public GatewaySession(String sessionId, boolean admin, ServerWebSocket webSocket) {
        super(sessionId, admin);
        this.webSocket = webSocket;
    }

    public GatewaySession(String sessionId, boolean admin, boolean test, ServerWebSocket webSocket) {
        super(sessionId, admin, test);
        this.webSocket = webSocket;
    }

    public void close() {
        try {
            webSocket.close();
        } catch (Exception ignored) {
        }
    }

    public ServerWebSocket getWebSocket() {
        return webSocket;
    }

    public UUID getRoomKey() {
        return roomKey;
    }

    public GatewaySession setRoomKey(UUID roomKey) {
        this.roomKey = roomKey;
        return this;
    }
}
