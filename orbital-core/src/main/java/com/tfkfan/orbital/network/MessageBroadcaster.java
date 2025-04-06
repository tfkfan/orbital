package com.tfkfan.orbital.network;

import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.network.message.MessageType;
import com.tfkfan.orbital.session.Session;
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
