package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.factory.GameStateFactory;
import com.tfkfan.orbital.factory.GameRoomFactory;
import com.tfkfan.orbital.factory.PlayerFactory;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.state.GameState;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.shared.ActionType;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GameManagerImpl implements GameManager {
    protected final Vertx vertx;
    protected final RoomConfig roomConfig;
    protected final String verticleId;

    protected final Map<UUID, GameRoom> gameRoomMap = new HashMap<>();
    protected final Map<String, PlayerSession> playerSessionsMap = new HashMap<>();

    protected final PlayerFactory playerFactory;
    protected final GameStateFactory gameStateFactory;
    protected final GameRoomFactory gameRoomFactory;

    public GameManagerImpl(String verticleId,
                           Vertx vertx,
                           RoomConfig roomConfig,
                           PlayerFactory playerFactory,
                           GameStateFactory gameStateFactory,
                           GameRoomFactory gameRoomFactory) {
        this.roomConfig = roomConfig;
        this.verticleId = verticleId;
        this.vertx = vertx;
        this.playerFactory = playerFactory;
        this.gameStateFactory = gameStateFactory;
        this.gameRoomFactory = gameRoomFactory;

        vertx.eventBus().consumer(Constants.ROOM_VERTICAL_CHANNEL, this::onMessage);
    }

    protected void onMessage(Message<JsonObject> message) {
        log.info("Received a message to room {} : {}", verticleId, message.body().encode());
        final JsonObject json = message.body();
        final String rawAction = json.getString(Fields.action);
        if (rawAction != null) {
            ActionType actionType = ActionType.valueOf(rawAction);
            if (actionType.equals(ActionType.NEW_ROOM)) {
                final GameState gameState = gameStateFactory.get();
                final UUID roomId = UUID.fromString(json.getString(Fields.roomId));
                final GameRoom room = gameRoomFactory.createGameRoom(verticleId, roomId, gameState, this, roomConfig);
                room.onCreate();

                json.getJsonArray(Fields.sessions)
                        .stream()
                        .forEach(s -> {
                            JsonObject session = (JsonObject) s;
                            final String sessionId = session
                                    .getString(Fields.sessionId);
                            final boolean isAdmin = session.getBoolean(Fields.admin);
                            final PlayerSession userSession = new PlayerSession(sessionId, isAdmin);
                            gameState.addPlayer(playerFactory.createPlayer(gameState.nextPlayerId(), room, userSession));
                            playerSessionsMap.put(sessionId, userSession);
                            room.onJoin(userSession);
                        });

                gameRoomMap.put(room.key(), room);
                room.onStart();
            }
        }
    }

    @Override
    public void onBattleEnd(GameRoom room) {
        gameRoomMap.remove(room.key());
        room.close();
    }
}
