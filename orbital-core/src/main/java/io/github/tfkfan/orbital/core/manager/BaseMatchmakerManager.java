package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.*;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.route.MessageRoute;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.github.tfkfan.orbital.core.shared.ActionType;
import io.github.tfkfan.orbital.core.shared.UniqueQueue;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseMatchmakerManager extends BaseManager implements MatchmakerManager {
    protected final RoomConfig roomConfig;
    private final UniqueQueue<GameRoomJoinEvent> playersQueue = new UniqueQueue<>();

    public BaseMatchmakerManager(Vertx vertx, RoomConfig roomConfig) {
        super(vertx);
        this.roomConfig = roomConfig;
        initConsumers();
    }

    protected void initConsumers() {
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL,
                msg -> log.debug("Room {} has been created and started", msg.body().getString(Fields.roomId)));
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL,
                msg -> log.debug("Room {} has been deleted", msg.body().getString(Fields.roomId)));
    }

    @Override
    public void onDisconnect(GatewaySession data) {
        playersQueue.removeIf(e -> e.getSession().getId().equals(data.getId()));
        publishRoomManagementEvent(data.getRoomKey(), ActionType.PLAYER_DISCONNECT, null, new JsonArray(
                Collections.singletonList(new JsonObject().put(Fields.sessionId, data.getId()))
        ));
    }

    @Override
    public void onConnect(GatewaySession data) {
    }

    //Default matchmaking implementation
    protected void join(GameRoomJoinEvent joinEvent) {
        final RoomType roomType = RoomType.getOrDefault(joinEvent.getData(), RoomType.BATTLE_ROYALE);

        if (RoomType.TRAINING.equals(roomType)) {
            handleJoinTraining(joinEvent);
            return;
        }

        handleJoin(roomType, joinEvent);
    }

    protected void handleJoinTraining(final GameRoomJoinEvent joinEvent) {
        requestRoomManagementCreateEvent(newRoomId(), RoomType.TRAINING, Collections.singletonList(joinEvent));
    }

    protected void handleJoin(final RoomType roomType, final GameRoomJoinEvent joinEvent) {
        playersQueue.add(joinEvent);

        joinEvent.getSession().send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomConfig.getMaxPlayers())
            return;

        requestRoomManagementCreateEvent(newRoomId(), roomType, playersQueue.chunk(roomConfig.getMaxPlayers()));
    }

    private UUID newRoomId() {
        return UUID.randomUUID();
    }

    protected void requestRoomManagementCreateEvent(final UUID roomId, final RoomType roomType, List<GameRoomJoinEvent> userSessions) {
        requestRoomManagementEvent(roomId, ActionType.NEW_ROOM, roomType, new JsonArray(userSessions.stream().map(e ->
                        new JsonObject()
                                .put(Fields.sessionId, e.getSession().getId())
                                .put(Fields.admin, e.getSession().isAdmin())
                                .put(Fields.initialData, e.getData()))
                .collect(Collectors.toList())))
                .onSuccess(t -> userSessions.forEach(it -> it.getSession().setRoomKey(roomId)))
                .onFailure(t -> {
                    log.error("Room verticle {} failed to join", roomId, t);
                    userSessions.forEach(it -> it.getSession().close());
                });
    }

    @MessageRoute(MessageTypes.GAME_ROOM_JOIN)
    private void addPlayerToWait(GatewaySession userSession, JsonObject initialData) {
        join(new GameRoomJoinEvent(userSession, initialData));
    }

    @MessageRoute(MessageTypes.PLAYER_KEY_DOWN)
    protected void onPlayerKeyDown(GatewaySession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, KeyDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_DOWN)
    protected void onPlayerMouseDown(GatewaySession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseDownPlayerEvent.class);
    }

    @MessageRoute(MessageTypes.PLAYER_MOUSE_MOVE)
    protected void onPlayerMouseMove(GatewaySession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, MouseMovePlayerEvent.class);
    }

    @MessageRoute(MessageTypes.GAME_ROOM_INFO)
    protected void onGameRoomInfo(GatewaySession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, GameRoomInfoEvent.class);
    }

    @MessageRoute(MessageTypes.INIT)
    protected void onInit(GatewaySession userSession, JsonObject data) {
        publishRoomEvent(userSession, data, InitPlayerEvent.class);
    }
}
