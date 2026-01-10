package io.github.tfkfan.orbital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.SneakyThrows;

import java.util.Map;

public interface MessageBuilder {
    ObjectMapper mapper = DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    static Message joinMessage(RoomType roomType) {
        return create(MessageTypes.GAME_ROOM_JOIN, Map.of("roomType", roomType.name()));
    }

    static Message create(Integer messageType) {
        return create(messageType, null);
    }

    static Message create(Integer messageType, Map<String, Object> data) {
        return new Message(messageType, data);
    }

    @SneakyThrows
    static Message read(String body) {
        return mapper.readValue(body, Message.class);
    }

    static Message joinWaitMessage() {
        return create(MessageTypes.GAME_ROOM_JOIN_WAIT);
    }

    static Message joinSuccessMessage() {
        return create(MessageTypes.GAME_ROOM_JOIN_SUCCESS);
    }
}
