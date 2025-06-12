package io.github.tfkfan.orbital.core.manager.impl;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.manager.BaseGatewayManager;
import io.github.tfkfan.orbital.core.manager.MatchmakerManager;
import io.github.tfkfan.orbital.core.manager.WebSocketManager;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.route.RouteProcessor;
import io.github.tfkfan.orbital.core.session.Session;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class WebSocketManagerImpl extends BaseGatewayManager implements WebSocketManager {
    private static final Logger log = LoggerFactory.getLogger(WebSocketManagerImpl.class);

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
        webSocket.closeHandler(t -> {
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

    @Override
    public void broadcast(JsonObject jsonObject) {

    }
}
