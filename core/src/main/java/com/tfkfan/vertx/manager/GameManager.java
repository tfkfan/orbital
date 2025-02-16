package com.tfkfan.vertx.manager;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.configuration.Fields;
import com.tfkfan.vertx.factory.GameMapFactory;
import com.tfkfan.vertx.factory.GameRoomFactory;
import com.tfkfan.vertx.factory.PlayerFactory;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.Player;
import com.tfkfan.vertx.game.room.GameRoom;
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
public class GameManager<
        P extends Player,
        GR extends GameRoom> {
    protected final Vertx vertx;
    protected final RoomProperties roomProperties;
    protected final String verticleId;

    protected final Map<UUID, GameRoom> gameRoomMap = new HashMap<>();
    protected final Map<String, UserSession> playerSessionsMap = new HashMap<>();

    protected final PlayerFactory<P, GR> playerFactory;
    protected final GameMapFactory gameMapFactory;
    protected final GameRoomFactory<GR> gameRoomFactory;

    public GameManager(String verticleId,
                       Vertx vertx,
                       RoomProperties roomProperties,
                       PlayerFactory<P, GR> playerFactory,
                       GameMapFactory gameMapFactory,
                       GameRoomFactory<GR> gameRoomFactory) {
        this.roomProperties = roomProperties;
        this.verticleId = verticleId;
        this.vertx = vertx;
        this.playerFactory = playerFactory;
        this.gameMapFactory = gameMapFactory;
        this.gameRoomFactory = gameRoomFactory;

        vertx.eventBus().consumer(Constants.ROOM_VERTICAL_CHANNEL + verticleId, this::onMessage);
    }

    protected void onMessage(Message<JsonObject> message) {
        log.info("Received a message to room {} : {}", verticleId, message.body().encode());
        final JsonObject json = message.body();
        if (json.containsKey(Fields.action)) {
            ActionType actionType = ActionType.valueOf(json.getString(Fields.action));
            if (actionType.equals(ActionType.CREATE)) {
                final GameMap gameMap = gameMapFactory.get();
                final UUID roomId = UUID.fromString(json.getString(Fields.roomId));
                final JsonArray sessions = json.getJsonArray(Fields.sessions);
                final GR room = gameRoomFactory.createGameRoom(verticleId, roomId, gameMap, this, roomProperties);
                final List<UserSession> roomUserSessions = new ArrayList<>();

                long lastPlayerId = 1L;
                for (int i = 0; i < sessions.size(); i++) {
                    final String sessionId = sessions
                            .getJsonObject(i)
                            .getString(Fields.sessionId);
                    final UserSession userSession = new UserSession(sessionId, verticleId);
                    gameMap.addPlayer(playerFactory.createPlayer(lastPlayerId++, room, userSession));
                    playerSessionsMap.put(sessionId, userSession);
                    roomUserSessions.add(userSession);
                }
                gameRoomMap.put(room.key(), room);
                room.onRoomCreated(roomUserSessions);
                room.onRoomStarted();
            }
        }
    }

    public void onBattleEnd(GameRoom room) {
        room.close();
        gameRoomMap.remove(room.key());
    }
}
