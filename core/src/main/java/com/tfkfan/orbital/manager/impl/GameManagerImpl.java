package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.factory.GameMapFactory;
import com.tfkfan.orbital.factory.GameRoomFactory;
import com.tfkfan.orbital.factory.PlayerFactory;
import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.model.players.Player;
import com.tfkfan.orbital.game.room.GameRoom;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.session.UserSession;
import com.tfkfan.orbital.shared.ActionType;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GameManagerImpl<P extends Player, GR extends GameRoom, RP> implements GameManager {
    protected final Vertx vertx;
    protected final RP roomProperties;
    protected final String verticleId;

    protected final Map<UUID, GameRoom> gameRoomMap = new HashMap<>();
    protected final Map<String, UserSession> playerSessionsMap = new HashMap<>();

    protected final PlayerFactory<P, GR> playerFactory;
    protected final GameMapFactory gameMapFactory;
    protected final GameRoomFactory<GR, RP> gameRoomFactory;

    public GameManagerImpl(String verticleId,
                           Vertx vertx,
                           RP roomProperties,
                           PlayerFactory<P, GR> playerFactory,
                           GameMapFactory gameMapFactory,
                           GameRoomFactory<GR, RP> gameRoomFactory) {
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
        final String rawAction = json.getString(Fields.action);
        if (rawAction != null) {
            ActionType actionType = ActionType.valueOf(rawAction);
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

    @Override
    public void onBattleEnd(GameRoom room) {
        log.info("onBattleEnd : {}", room.key());
        room.close();
        gameRoomMap.remove(room.key());
    }

    @Override
    public void onBattleStart(GameRoom room) {
        log.info("onBattleStart : {}", room.key());
    }
}
