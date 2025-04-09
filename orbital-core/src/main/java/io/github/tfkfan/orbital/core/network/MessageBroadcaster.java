package io.github.tfkfan.orbital.core.network;

import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.session.Session;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @author Baltser Artem tfkfan
 */
public interface MessageBroadcaster {
    void broadcast(MessageType type, String message);
    void broadcast(Message message);
    void broadcast(int messageType, Object content);
    void broadcast(Function<Session, JsonObject> messageFunction);
    void broadcast(JsonObject jsonObject);
}
