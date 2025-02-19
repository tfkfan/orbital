package com.tfkfan.vertx.manager;

import com.tfkfan.vertx.configuration.MessageTypes;
import com.tfkfan.vertx.event.GameRoomInfoEvent;
import com.tfkfan.vertx.event.InitPlayerEvent;
import com.tfkfan.vertx.properties.RoomProperties;
import com.tfkfan.vertx.route.MessageRoute;
import com.tfkfan.vertx.session.UserSession;
import io.vertx.core.json.JsonObject;

public class DefaultMatchmakerManager extends MatchmakerManager{
    public DefaultMatchmakerManager(RoomProperties roomProperties) {
        super(roomProperties);
    }

    @MessageRoute(MessageTypes.GAME_ROOM_INFO)
    protected void onGameRoomInfo(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, GameRoomInfoEvent.class);
    }

    @MessageRoute(MessageTypes.INIT)
    protected void onInit(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, InitPlayerEvent.class);
    }
}
