package io.github.tfkfan.orbital.core.manager.impl;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.factory.GameRoomFactory;
import io.github.tfkfan.orbital.core.factory.GameStateFactory;
import io.github.tfkfan.orbital.core.factory.PlayerFactory;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.metrics.registrar.GameManagerMetricsRegistrar;
import io.github.tfkfan.orbital.core.model.players.Player;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.shared.ActionType;
import io.github.tfkfan.orbital.core.state.GameState;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.backends.BackendRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GameManagerImpl<R extends GameRoom, S extends GameState> implements GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManagerImpl.class);

    protected final Vertx vertx;
    protected final RoomConfig roomConfig;
    protected final String verticleId;

    protected final Map<UUID, GameRoom> gameRoomMap = new HashMap<>();
    protected final Map<String, PlayerSession> playerSessionsMap = new HashMap<>();

    protected final PlayerFactory playerFactory;
    protected final GameStateFactory<S> gameStateFactory;
    protected final GameRoomFactory<R, S> gameRoomFactory;

    public GameManagerImpl(String verticleId,
                           Vertx vertx,
                           RoomConfig roomConfig,
                           PlayerFactory playerFactory,
                           GameStateFactory<S> gameStateFactory,
                           GameRoomFactory<R, S> gameRoomFactory) {
        this.verticleId = Objects.requireNonNull(verticleId);
        this.roomConfig = Objects.requireNonNull(roomConfig);
        this.vertx = Objects.requireNonNull(vertx);
        this.playerFactory = Objects.requireNonNull(playerFactory);
        this.gameStateFactory = Objects.requireNonNull(gameStateFactory);
        this.gameRoomFactory = Objects.requireNonNull(gameRoomFactory);

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
                final S gameState = gameStateFactory.get();
                final RoomType roomType = RoomType.valueOf(json.getString(Fields.roomType));
                final UUID roomId = UUID.fromString(json.getString(Fields.roomId));
                final GameRoom room = createRoom(roomId, roomType, gameState, json.getJsonArray(Fields.sessions));
                room.onStart();
                message.reply(new JsonObject().put(Fields.roomId, roomId.toString()),
                        new DeliveryOptions().setLocalOnly(true).setSendTimeout(1000));
            }
        }
    }

    protected GameRoom createRoom(final UUID roomId, final RoomType roomType, final S gameState, JsonArray playersSessions) {
        validatePlayersCount(roomType, playersSessions, roomId);

        final GameRoom room = gameRoomFactory.createGameRoom(verticleId, roomId, roomType,
                gameState, this, roomConfig);
        room.onCreate();

        playersSessions.forEach(s -> addPlayerSession(gameState, room, (JsonObject) s));

        if (RoomType.TRAINING.equals(roomType))
            addNpcSessions(gameState, room, roomConfig.getMaxPlayers() - 1);

        gameRoomMap.put(room.key(), room);
        return room;
    }

    protected void addNpcSessions(S gameState, GameRoom room, int count) {
        for (int id = 0; id < count; id++)
            addPlayerSession(gameState, room, Integer.toString(id),
                    false,
                    true,
                    null);
    }

    protected void postPlayerSessionHandle(final Player player) {

    }

    protected void addPlayerSession(S gameState, GameRoom room, JsonObject session) {
        addPlayerSession(gameState, room, session
                        .getString(Fields.sessionId),
                session.getBoolean(Fields.admin),
                false,
                session.getJsonObject(Fields.initialData));
    }

    protected void addPlayerSession(S gameState, GameRoom room, String sessionId,
                                    boolean isAdmin, boolean isNpc,
                                    JsonObject initialData) {
        final PlayerSession userSession = new PlayerSession(sessionId, isAdmin, isNpc);
        final Player player = playerFactory.createPlayer(gameState.nextPlayerId(),
                room, userSession, initialData);
        gameState.addPlayer(player);
        playerSessionsMap.put(sessionId, userSession);
        room.onJoin(userSession);
        postPlayerSessionHandle(player);
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

    protected void validatePlayersCount(final RoomType roomType, final JsonArray playersSessions, final UUID roomId) {
        if (RoomType.TRAINING.equals(roomType) && playersSessions.size() > 1 || !RoomType.TRAINING.equals(roomType) && playersSessions.size() >= roomConfig.getMaxPlayers())
            throw new IllegalArgumentException("Invalid players count received for room: " + roomId);

    }
}
