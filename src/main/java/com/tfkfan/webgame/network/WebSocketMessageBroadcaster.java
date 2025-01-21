package com.tfkfan.webgame.network;

import com.tfkfan.webgame.network.message.Message;
import com.tfkfan.webgame.network.message.MessageType;
import com.tfkfan.webgame.session.UserSession;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @author Baltser Artem tfkfan
 */
public interface WebSocketMessageBroadcaster {
    void broadcast(MessageType type, String message);
    void broadcast(Message message);
    void broadcast(int messageType, Object content);
    void broadcast(Function<UserSession, JsonObject> messageFunction);
}
