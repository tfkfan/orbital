package io.github.tfkfan.orbital.core.network;

import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.vertx.core.json.JsonObject;

public interface MessageSender {
    void sendTo(String address, JsonObject message);

    void send(int messageType, Object content);

    void send(Message message);

    void send(JsonObject message);

    void sendErrorMessage(String errorMsg);

    void sendText(MessageType type, String message);

    void sendText(String message);

    default JsonObject processBeforeSend(JsonObject message) {
        if(!message.containsKey(Fields.timestamp))
            message.put(Fields.timestamp, System.currentTimeMillis());
        return message;
    }
}
