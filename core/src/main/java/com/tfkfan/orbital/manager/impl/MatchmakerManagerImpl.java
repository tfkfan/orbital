package com.tfkfan.orbital.manager.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.event.KeyDownPlayerEvent;
import com.tfkfan.orbital.event.MouseDownPlayerEvent;
import com.tfkfan.orbital.event.MouseMovePlayerEvent;
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
    final Integer roomMaxPlayers;
    final Queue<Pair<UserSession, JsonObject>> playersQueue = new ArrayDeque<>();
    final Map<UUID, Boolean> gameRoomMap = new HashMap<>();
    final List<String> roomVerticleIds = new ArrayList<>();
    final SessionListener sessionListener = new SessionListener() {
        @Override
        public void onDisconnect(UserSession data) {
            playersQueue.removeIf(e -> e.getA().getId().equals(data.getId()));
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

    public MatchmakerManagerImpl(Integer roomMaxPlayers) {
        this.roomMaxPlayers = roomMaxPlayers;
    }

    String getNextRoomVerticleId() {
        if (currentRoomVerticleIndex >= roomVerticleIds.size())
            currentRoomVerticleIndex = 0;
        return roomVerticleIds.get(currentRoomVerticleIndex++);
    }

    void onPlayerWait(UserSession userSession, JsonObject initialData) {
        playersQueue.add(new Pair<>(userSession, initialData));
        userSession.send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomMaxPlayers || roomVerticleIds.isEmpty())
            return;

        final EventBus eventBus = Vertx.currentContext().owner().eventBus();
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL, msg -> log.info("Room {} has been created and started", msg.body().getString(Fields.roomId)));

        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL, msg -> {
            final UUID roomId = UUID.fromString(msg.body().getString(Fields.roomId));
            gameRoomMap.remove(roomId);
            userSession.setRoomKey(null);
        });

        final UUID roomId = UUID.randomUUID();
        final String nextVerticleId = getNextRoomVerticleId();

        gameRoomMap.put(roomId, true);
        userSession.setRoomKey(roomId);
        userSession.setRoomVerticleId(nextVerticleId);

        final List<Pair<UserSession, JsonObject>> userSessions = new ArrayList<>();
        while (userSessions.size() != roomMaxPlayers)
            userSessions.add(playersQueue.remove());

        log.info("Sending create room at room verticle: {}", nextVerticleId);
        eventBus.publish(Constants.ROOM_VERTICAL_CHANNEL + nextVerticleId, new JsonObject()
                .put(Fields.action, ActionType.CREATE)
                .put(Fields.roomId, roomId.toString())
                .put(Fields.sessions, new JsonArray(userSessions.stream().map(e ->
                                new JsonObject()
                                        .put(Fields.sessionId, e.getA().getId())
                                        .put(Fields.initialData, e.getB()))
                        .collect(Collectors.toList()))));
    }

    @MessageRoute(MessageTypes.GAME_ROOM_JOIN)
    private void addPlayerToWait(UserSession userSession, JsonObject initialData) {
        onPlayerWait(userSession, initialData);
    }

    @MessageRoute(MessageTypes.PLAYER_KEY_DOWN)
    private void onPlayerKeyDown(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, KeyDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_DOWN)
    private void onPlayerMouseDown(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_MOVE)
    private void onPlayerMouseMove(UserSession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseMovePlayerEvent.class);
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
