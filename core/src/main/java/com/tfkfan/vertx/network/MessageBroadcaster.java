package com.tfkfan.vertx.network;

import com.tfkfan.vertx.network.message.Message;
import com.tfkfan.vertx.network.message.MessageType;
import com.tfkfan.vertx.session.UserSession;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @author Baltser Artem tfkfan
 */
public interface MessageBroadcaster {
    void broadcast(MessageType type, String message);
    void broadcast(Message message);
    void broadcast(int messageType, Object content);
    void broadcast(Function<UserSession, JsonObject> messageFunction);
}
