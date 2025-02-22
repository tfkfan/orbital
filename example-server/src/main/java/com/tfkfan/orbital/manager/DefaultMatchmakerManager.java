package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.event.GameRoomInfoEvent;
import com.tfkfan.orbital.event.InitPlayerEvent;
import com.tfkfan.orbital.manager.impl.MatchmakerManagerImpl;
import com.tfkfan.orbital.route.MessageRoute;
import com.tfkfan.orbital.session.UserSession;
import io.vertx.core.json.JsonObject;

public class DefaultMatchmakerManager extends MatchmakerManagerImpl {
    public DefaultMatchmakerManager(int roomMaxPlayers) {
        super(roomMaxPlayers);
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
