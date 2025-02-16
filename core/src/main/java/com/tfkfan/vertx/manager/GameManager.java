package com.tfkfan.vertx.manager;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.configuration.Fields;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.DefaultPlayer;
import com.tfkfan.vertx.game.room.DefaultGameRoom;
import com.tfkfan.vertx.properties.RoomProperties;
import com.tfkfan.vertx.session.UserSession;
import com.tfkfan.vertx.shared.ActionType;
import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.configuration.Fields;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.DefaultPlayer;
import com.tfkfan.vertx.game.room.DefaultGameRoom;
import com.tfkfan.vertx.properties.RoomProperties;
import com.tfkfan.vertx.session.UserSession;
import com.tfkfan.vertx.shared.ActionType;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GameManager {
    final Vertx vertx;
    final RoomProperties roomProperties;
    final String verticleId;

    final Map<UUID, DefaultGameRoom> gameRoomMap = new HashMap<>();
    final Map<String, UserSession> playerSessionsMap = new HashMap<>();

    public GameManager(String verticleId, Vertx vertx, RoomProperties roomProperties) {
        this.roomProperties = roomProperties;
        this.verticleId = verticleId;
        this.vertx = vertx;
        vertx.eventBus().consumer(Constants.ROOM_VERTICAL_CHANNEL + verticleId, this::onMessage);
    }

    private void onMessage(Message<JsonObject> message) {
        log.info("Received a message to room {} : {}", verticleId, message.body().encode());
        final JsonObject json = message.body();
        if (json.containsKey(Fields.action)) {
            ActionType actionType = ActionType.valueOf(json.getString(Fields.action));
            if (actionType.equals(ActionType.CREATE)) {
                final GameMap gameMap = new GameMap();
                final UUID roomId = UUID.fromString(json.getString(Fields.roomId));
                final JsonArray sessions = json.getJsonArray(Fields.sessions);
                final DefaultGameRoom room = new DefaultGameRoom(verticleId, roomId, this, gameMap, vertx, roomProperties);
                final List<UserSession> roomUserSessions = new ArrayList<>();

                long lastPlayerId = 1L;
                for (int i = 0; i < sessions.size(); i++) {
                    final JsonObject sessionData = sessions.getJsonObject(i);
                    final String sessionId = sessionData.getString(Fields.sessionId);
                    final UserSession userSession = new UserSession(sessionId, verticleId);
                    final DefaultPlayer player = new DefaultPlayer(lastPlayerId++, room, userSession);
                    gameMap.addPlayer(player);
                    playerSessionsMap.put(sessionId, userSession);
                    roomUserSessions.add(userSession);
                }
                gameRoomMap.put(room.key(), room);
                room.onRoomCreated(roomUserSessions);
                room.onRoomStarted();
            }
        }
    }

    public void onBattleEnd(DefaultGameRoom room) {
        room.close();
        gameRoomMap.remove(room.key());
    }
}
