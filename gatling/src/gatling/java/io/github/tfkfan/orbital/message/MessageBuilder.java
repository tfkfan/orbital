package io.github.tfkfan.orbital.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.vertx.core.json.jackson.DatabindCodec;

import java.util.Map;

public interface MessageBuilder {
    ObjectMapper mapper = DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    static String joinMessage(RoomType roomType) {
        return create(MessageTypes.GAME_ROOM_JOIN, Map.of("roomType", roomType.name())).toString();
    }

    static String joinWaitMessage(){
        return create(MessageTypes.GAME_ROOM_JOIN_WAIT).toString();
    }

    static Message create(Integer messageType){
        return create(messageType,null);
    }

    static Message create(Integer messageType, Map<String, Object> data) {
        return new Message(messageType, data);
    }
}
