package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.shared.ActionType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.UUID;

import static io.github.tfkfan.orbital.core.verticle.BaseVerticle.defaults;

public abstract class BaseManager implements Manager {
    protected final Vertx vertx;
    protected final EventBus eventBus;

    protected BaseManager(Vertx vertx) {
        this.vertx = vertx;
        eventBus = vertx.eventBus();
    }

    public Future<Message<JsonObject>> requestRoomManagementEvent(final UUID roomId,
                                                                  final ActionType actionType,
                                                                  final RoomType roomType,
                                                                  final JsonArray userSessions) {
        return eventBus.request(Constants.ROOM_VERTICAL_CHANNEL, makeData(roomId, actionType, roomType, userSessions), defaults());
    }

    public void publishRoomManagementEvent(final UUID roomId,
                                           final ActionType actionType,
                                           final RoomType roomType,
                                           final JsonArray userSessions) {
        eventBus.publish(Constants.ROOM_VERTICAL_CHANNEL, makeData(roomId, actionType, roomType, userSessions), defaults());
    }

    private JsonObject makeData(final UUID roomId,
                                final ActionType actionType,
                                final RoomType roomType,
                                final JsonArray userSessions) {
        final JsonObject data = new JsonObject()
                .put(Fields.action, actionType)
                .put(Fields.roomId, roomId != null ? roomId.toString() : null)
                .put(Fields.sessions, userSessions);

        if (Objects.nonNull(roomType))
            data.put(Fields.roomType, roomType.toString());

        return data;
    }
}
