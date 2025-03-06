package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.manager.BaseGatewayManager;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.manager.WebSocketManager;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.network.message.MessageType;
import com.tfkfan.orbital.route.RouteProcessor;
import com.tfkfan.orbital.session.UserSession;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class WebSocketManagerImpl extends BaseGatewayManager implements WebSocketManager {
    protected final Vertx vertx = Vertx.currentContext().owner();
    protected final Map<String, List<MessageConsumer<?>>> messageConsumers = new HashMap<>();
    protected final RouteProcessor routeProcessor;

    public WebSocketManagerImpl(MatchmakerManager matchmakerManager) {
        super(matchmakerManager);
        this.routeProcessor = new RouteProcessor(matchmakerManager);
    }

    @Override
    public void handle(ServerWebSocket webSocket) {
        if (!webSocket.path().equals(Constants.WEBSOCKET_PATH)) {
            log.debug("Wrong path");
            webSocket.reject();
            return;
        }

        final UserSession session = new UserSession();
        getSessionListener().onConnect(session);

        final String BROADCAST_CONSUMER = Constants.WS_CHANNEL;
        final String SESSION_CONSUMER = Constants.WS_SESSION_CHANNEL + session.getId();

        final List<MessageConsumer<?>> consumers = Arrays.asList(
                vertx.eventBus()
                        .<JsonObject>consumer(BROADCAST_CONSUMER,
                                message -> webSocket.writeTextMessage(message.body().encode())),
                vertx.eventBus()
                        .<JsonObject>consumer(SESSION_CONSUMER,
                                message -> webSocket.writeTextMessage(message.body().encode()))
        );

        messageConsumers.put(session.getId(), consumers);

        webSocket.textMessageHandler(message -> {
            try {
                this.onMessage(session, new JsonObject(message));
            } catch (Exception e) {
                log.error("Internal error", e);
            }
        });
        webSocket.exceptionHandler(throwable -> log.error("Internal error", throwable));
        webSocket.closeHandler(_ -> onDisconnect(session));
    }

    protected void onDisconnect(UserSession session) {
        getSessionListener().onDisconnect(session);
        if (messageConsumers.containsKey(session.getId())) {
            messageConsumers.get(session.getId()).forEach(MessageConsumer::unregister);
            messageConsumers.remove(session.getId());
        }
    }

    private void onMessage(UserSession userSession, JsonObject message) throws InvocationTargetException, IllegalAccessException {
        routeProcessor.execute(userSession, message.getInteger(Fields.type), message.getJsonObject(Fields.data));
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
    public void broadcast(Function<UserSession, JsonObject> messageFunction) {
    }
}
