package io.github.tfkfan.orbital.core.network;

import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.event.Event;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public interface RoomEventPublisher {
    default <T extends Event> void publishRoomEvent(PlayerSession playerSession, JsonObject data, Class<T> eventClass) {
        Vertx.currentContext().owner().eventBus().publish(GameRoom.constructEventListenerConsumer(playerSession.getPlayer().getGameRoom().key(), eventClass), data,
                new DeliveryOptions().addHeader(Fields.sessionId, playerSession.getId()));
    }

    default <T extends Event> void publishRoomEvent(GatewaySession userSession, JsonObject data, Class<T> eventClass) {
        Vertx.currentContext().owner().eventBus().publish(GameRoom.constructEventListenerConsumer(userSession.getRoomKey(), eventClass), data,
                new DeliveryOptions().addHeader(Fields.sessionId, userSession.getId()));
    }

    default <T extends Event> void publishRoomEvent(GatewaySession userSession, Event data) {
        publishRoomEvent(userSession, JsonObject.mapFrom(data), data.getClass());
    }

    default <T extends Event> void publishRoomEvent(PlayerSession userSession, Event data) {
        publishRoomEvent(userSession, JsonObject.mapFrom(data), data.getClass());
    }
}
