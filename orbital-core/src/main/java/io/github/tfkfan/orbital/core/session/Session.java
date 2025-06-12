package io.github.tfkfan.orbital.core.session;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.MessageSender;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.network.pack.shared.GameMessagePack;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.UUID;


public abstract class Session implements MessageSender {
    private final String id;
    private final boolean npc;
    private final boolean admin;

    public Session() {
        this(UUID.randomUUID().toString(), false, false);
    }

    public Session(boolean admin) {
        this(UUID.randomUUID().toString(), admin, false);
    }

    public Session(String sessionId, boolean admin) {
        this(sessionId, admin, false);
    }

    public Session(String sessionId, boolean admin, boolean npc) {
        this.id = sessionId;
        this.npc = npc;
        this.admin = admin;
    }

    @Override
    public void sendTo(String address, JsonObject message) {
        Vertx.currentContext().owner().eventBus().publish(address,
                JsonObject.mapFrom(message));
    }

    @Override
    public void send(int messageType, Object content) {
        Object data = content;
        if (!(content instanceof String) && !(content instanceof JsonObject))
            data = JsonObject.mapFrom(content);

        Vertx.currentContext().owner().eventBus().publish(
                Constants.sessionConsumer(Constants.GAME_ADDRESS, id),
                new JsonObject().put(Fields.type, messageType)
                        .put(Fields.data, data));
    }

    @Override
    public void send(Message message) {
        Vertx.currentContext().owner().eventBus().publish(Constants.sessionConsumer(Constants.GAME_ADDRESS, id),
                JsonObject.mapFrom(message));
    }

    @Override
    public void send(JsonObject message) {
        Vertx.currentContext().owner().eventBus().publish(Constants.sessionConsumer(Constants.GAME_ADDRESS, id), message);
    }

    @Override
    public void sendErrorMessage(String errorMsg) {
        /* if (socket == null) return;*/
    }

    @Override
    public void sendText(MessageType type, String message) {
        send(MessageTypes.MESSAGE, new GameMessagePack(type.getType(), message));
    }

    @Override
    public void sendText(String message) {
        sendText(MessageType.SYSTEM, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getId() {
        return id;
    }

    public boolean isNpc() {
        return npc;
    }

    public boolean isAdmin() {
        return admin;
    }
}
