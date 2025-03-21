package com.tfkfan.orbital.network;

import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.event.Event;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.session.GatewaySession;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public interface RoomEventPublisher {
    default <T extends Event> void publishRoomEvent(GatewaySession userSession, JsonObject data, Class<T> eventClass) {
        Vertx.currentContext().owner().eventBus().publish(GameRoom.constructEventListenerConsumer(userSession.getRoomKey(), eventClass), data,
                new DeliveryOptions().addHeader(Fields.sessionId, userSession.getId()));
    }

    default <T extends Event> void publishRoomEvent(GatewaySession userSession, Event data) {
        publishRoomEvent(userSession, JsonObject.mapFrom(data), data.getClass());
    }
}
