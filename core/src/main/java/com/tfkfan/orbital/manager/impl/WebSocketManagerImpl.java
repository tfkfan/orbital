package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.manager.BaseGatewayManager;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.manager.WebSocketManager;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.network.message.MessageType;
import com.tfkfan.orbital.route.RouteProcessor;
import com.tfkfan.orbital.session.Session;
import com.tfkfan.orbital.session.GatewaySession;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class WebSocketManagerImpl extends BaseGatewayManager implements WebSocketManager {
    protected final Vertx vertx = Vertx.currentContext().owner();
    protected final RouteProcessor routeProcessor;

    public WebSocketManagerImpl(MatchmakerManager matchmakerManager) {
        super(matchmakerManager);
        this.routeProcessor = new RouteProcessor(matchmakerManager);
    }

    @Override
    public void handle(ServerWebSocket webSocket) {
        switch (webSocket.path()) {
            case Constants.WS_ADMIN_PATH -> onConnect(Constants.ADMIN_ADDRESS, webSocket);
            case Constants.WS_GAME_PATH -> onConnect(Constants.GAME_ADDRESS, webSocket);
            default -> {
                log.error("Wrong path: {}", webSocket.path());
                webSocket.reject();
            }
        }
    }

    protected void onConnect(String address, ServerWebSocket webSocket) {
        final GatewaySession session = new GatewaySession(address.equals(Constants.ADMIN_ADDRESS), webSocket);
        matchmakerManager.onConnect(session);

        final MessageConsumer<?> broadcastConsumer = vertx.eventBus()
                .<JsonObject>consumer(Constants.broadcastConsumer(address),
                        message -> webSocket.writeTextMessage(message.body().encode()));
        final MessageConsumer<?> sessionConsumer = vertx.eventBus()
                .<JsonObject>localConsumer(Constants.sessionConsumer(address, session.getId()),
                        message -> webSocket.writeTextMessage(message.body().encode()));

        webSocket.textMessageHandler(message -> this.onMessage(session, new JsonObject(message)));
        webSocket.exceptionHandler(throwable -> log.error("Internal error", throwable));
        webSocket.closeHandler(_ -> {
            onDisconnect(session);
            broadcastConsumer.unregister();
            sessionConsumer.unregister();
        });
    }

    protected void onDisconnect(GatewaySession session) {
        matchmakerManager.onDisconnect(session);
    }

    private void onMessage(GatewaySession userSession, JsonObject message) {
        try {
            routeProcessor.execute(userSession, message.getInteger(Fields.type), message.getJsonObject(Fields.data));
        } catch (Exception e) {
            log.error("Internal error", e);
        }
    }

    @Override
    public void broadcast(MessageType type, String message) {
        log.error("broadcast not implemented yet");
    }

    @Override
    public void broadcast(Message message) {
        vertx.eventBus().publish(Constants.WS_CHANNEL, message);
    }

    @Override
    public void broadcast(int messageType, Object content) {
        vertx.eventBus().publish(Constants.WS_CHANNEL, JsonObject.mapFrom(content).put(Fields.type, messageType));
    }

    @Override
    public void broadcast(Function<Session, JsonObject> messageFunction) {
    }
}
