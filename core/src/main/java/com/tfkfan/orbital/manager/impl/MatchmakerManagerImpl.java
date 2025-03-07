package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.*;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.network.SessionListener;
import com.tfkfan.orbital.network.VerticleListener;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.route.MessageRoute;
import com.tfkfan.orbital.session.UserSession;
import com.tfkfan.orbital.shared.ActionType;
import com.tfkfan.orbital.shared.Pair;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MatchmakerManagerImpl implements MatchmakerManager {
    final Vertx vertx;
    final EventBus eventBus;
    final RoomConfig roomConfig;
    final Queue<Pair<UserSession, JsonObject>> playersQueue = new ArrayDeque<>();
    final Map<UUID, Boolean> gameRoomMap = new HashMap<>();
    final List<String> roomVerticleIds = new ArrayList<>();
    final SessionListener sessionListener = new SessionListener() {
        @Override
        public void onDisconnect(UserSession data) {
            playersQueue.removeIf(e -> e.a().getId().equals(data.getId()));
        }

        @Override
        public void onConnect(UserSession data) {

        }
    };

    final VerticleListener verticleListener = new VerticleListener() {
        @Override
        public void onDisconnect(String data) {
            roomVerticleIds.remove(data);
            log.error("Room verticle {} stopped. Remains: {}", data, roomVerticleIds);
        }

        @Override
        public void onConnect(String data) {
            roomVerticleIds.add(data);
            log.info("Room verticle {} connected. Remains: {}", data, roomVerticleIds);
        }
    };

    int currentRoomVerticleIndex = 0;

    public MatchmakerManagerImpl(RoomConfig roomConfig) {
        this.roomConfig = roomConfig;
        vertx = Vertx.currentContext().owner();
        eventBus = vertx.eventBus();
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL, msg -> {
            final UUID roomId = UUID.fromString(msg.body().getString(Fields.roomId));
            gameRoomMap.put(roomId, true);
            log.info("Room {} has been created and started", roomId);
        });
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL, msg -> {
            final UUID roomId = UUID.fromString(msg.body().getString(Fields.roomId));
            gameRoomMap.remove(roomId);
            log.info("Room {} has been deleted", roomId);
        });
    }

    String getNextRoomVerticleId() {
        if (currentRoomVerticleIndex >= roomVerticleIds.size())
            currentRoomVerticleIndex = 0;
        return roomVerticleIds.get(currentRoomVerticleIndex++);
    }

    void onPlayerWait(UserSession userSession, JsonObject initialData) {
        playersQueue.add(new Pair<>(userSession, initialData));
        userSession.send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomConfig.getMaxPlayers() || roomVerticleIds.isEmpty())
            return;

        final UUID roomId = UUID.randomUUID();
        final String nextVerticleId = getNextRoomVerticleId();

        userSession.setRoomKey(roomId);
        userSession.setRoomVerticleId(nextVerticleId);

        final List<Pair<UserSession, JsonObject>> userSessions = new ArrayList<>();
        while (userSessions.size() != roomConfig.getMaxPlayers())
            userSessions.add(playersQueue.remove());

        log.info("Sending create room at room verticle: {}", nextVerticleId);
        eventBus.publish(Constants.ROOM_VERTICAL_CHANNEL + nextVerticleId, new JsonObject()
                .put(Fields.action, ActionType.NEW_ROOM)
                .put(Fields.roomId, roomId.toString())
                .put(Fields.sessions, new JsonArray(userSessions.stream().map(e ->
                                new JsonObject()
                                        .put(Fields.sessionId, e.a().getId())
                                        .put(Fields.initialData, e.b()))
                        .collect(Collectors.toList()))));
    }

    @MessageRoute(MessageTypes.GAME_ROOM_JOIN)
    private void addPlayerToWait(UserSession userSession, JsonObject initialData) {
        onPlayerWait(userSession, initialData);
    }

    @MessageRoute(MessageTypes.PLAYER_KEY_DOWN)
    protected void onPlayerKeyDown(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, KeyDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_DOWN)
    protected void onPlayerMouseDown(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_MOVE)
    protected void onPlayerMouseMove(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseMovePlayerEvent.class);
    }

    @MessageRoute(MessageTypes.GAME_ROOM_INFO)
    protected void onGameRoomInfo(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, GameRoomInfoEvent.class);
    }

    @MessageRoute(MessageTypes.INIT)
    protected void onInit(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, InitPlayerEvent.class);
    }

    @Override
    public VerticleListener getVerticleListener() {
        return verticleListener;
    }

    @Override
    public SessionListener getSessionListener() {
        return sessionListener;
    }
}
