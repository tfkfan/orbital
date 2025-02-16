package com.tfkfan.vertx.manager;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.configuration.Fields;
import com.tfkfan.vertx.configuration.MessageTypes;
import com.tfkfan.vertx.event.Event;
import com.tfkfan.vertx.event.GameRoomInfoEvent;
import com.tfkfan.vertx.event.InitPlayerEvent;
import com.tfkfan.vertx.event.KeyDownPlayerEvent;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.network.message.Message;
import com.tfkfan.vertx.properties.RoomProperties;
import com.tfkfan.vertx.route.MessageRoute;
import com.tfkfan.vertx.session.UserSession;
import com.tfkfan.vertx.shared.ActionType;
import com.tfkfan.vertx.shared.Pair;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MatchmakerManager extends WebSocketManager {
    protected final RoomProperties roomProperties;
    protected final Queue<Pair<UserSession, JsonObject>> playersQueue = new ArrayDeque<>();
    protected final Map<UUID, Boolean> gameRoomMap = new HashMap<>();
    protected  final List<String> roomVerticleIds = new ArrayList<>();

    int currentRoomVerticleIndex = 0;

    public MatchmakerManager(RoomProperties roomProperties) {
        this.roomProperties = roomProperties;
    }

    public void onVerticleConnected(String roomVerticleId){
        roomVerticleIds.add(roomVerticleId);
    }

    public void onVerticleDisconnected(String roomVerticleId){
        roomVerticleIds.remove(roomVerticleId);
        log.error("Room verticle {} stopped. Remains: {}", roomVerticleId, roomVerticleIds);
    }

    private String getNextRoomVerticleId() {
        if (currentRoomVerticleIndex >= roomVerticleIds.size())
            currentRoomVerticleIndex = 0;
        return roomVerticleIds.get(currentRoomVerticleIndex++);
    }

    @MessageRoute(MessageTypes.GAME_ROOM_JOIN)
    private void addPlayerToWait(UserSession userSession, JsonObject initialData) {
        playersQueue.add(new Pair<>(userSession, initialData));
        userSession.send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomProperties.getMaxPlayers() || roomVerticleIds.isEmpty())
            return;

        final EventBus eventBus = vertx.eventBus();
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
        while (userSessions.size() != roomProperties.getMaxPlayers())
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

    @Override
    protected void onDisconnect(UserSession session) {
        playersQueue.removeIf(e -> e.getA().getId().equals(session.getId()));
    }

    @MessageRoute(MessageTypes.GAME_ROOM_INFO)
    private void onGameRoomInfo(UserSession userSession, JsonObject data) {
        onRoomEvent(userSession, data, GameRoomInfoEvent.class);
    }

    @MessageRoute(MessageTypes.INIT)
    private void onInit(UserSession userSession, JsonObject data) {
        onRoomEvent(userSession, data, InitPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_KEY_DOWN)
    private void onPlayerKeyDown(UserSession userSession, JsonObject data) {
        onRoomEvent(userSession, data, KeyDownPlayerEvent.class);
    }

    private <T extends Event> void onRoomEvent(UserSession userSession, JsonObject data, Class<T> eventClass) {
        vertx.eventBus().publish(GameRoom.constructEventListenerConsumer(userSession.getRoomKey(), eventClass), data,
                new DeliveryOptions().addHeader(Fields.sessionId, userSession.getId()));
    }
}
