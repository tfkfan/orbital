package io.github.tfkfan.orbital.core.session;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.EventBusAddressConstructor;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.MessageSender;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.network.pack.shared.GameMessagePack;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public abstract class Session implements MessageSender {
    private final String id;
    private final Map<String, String> attributes = new HashMap<>();
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
        Vertx.currentContext().owner().eventBus().publish(address, message);
    }

    @Override
    public void send(int messageType, Object content) {
        Object data = content;
        if (!(content instanceof String) && !(content instanceof JsonObject))
            data = JsonObject.mapFrom(content);

        sendTo(EventBusAddressConstructor.sessionConsumer(id), new JsonObject().put(Fields.type, messageType)
                .put(Fields.data, data));
    }

    @Override
    public void send(Message message) {
        sendTo(EventBusAddressConstructor.sessionConsumer(id), JsonObject.mapFrom(message));
    }

    @Override
    public void send(JsonObject message) {
        sendTo(EventBusAddressConstructor.sessionConsumer(id), message);
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

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}
