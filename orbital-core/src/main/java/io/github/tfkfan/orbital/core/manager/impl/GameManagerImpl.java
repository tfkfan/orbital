package io.github.tfkfan.orbital.core.manager.impl;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.factory.GameRoomFactory;
import io.github.tfkfan.orbital.core.factory.GameStateFactory;
import io.github.tfkfan.orbital.core.factory.PlayerFactory;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.metrics.registrar.GameManagerMetricsRegistrar;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.shared.ActionType;
import io.github.tfkfan.orbital.core.state.GameState;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.backends.BackendRegistries;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        new GameManagerMetricsRegistrar(BackendRegistries.getDefaultNow(), this).register();
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
        room.close().forEach(it -> playerSessionsMap.remove(it.getId()));
    }

    public Integer totalRooms() {
        return gameRoomMap.size();
    }

    public Integer totalPlayers() {
        return playerSessionsMap.size();
    }

    @Override
    public String id() {
        return verticleId;
    }
}
