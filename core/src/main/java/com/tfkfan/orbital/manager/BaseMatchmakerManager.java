package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.*;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.route.MessageRoute;
import com.tfkfan.orbital.session.GatewaySession;
import com.tfkfan.orbital.shared.ActionType;
import com.tfkfan.orbital.shared.UniqueQueue;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseMatchmakerManager implements MatchmakerManager {
    protected final Vertx vertx;
    protected final EventBus eventBus;
    protected final RoomConfig roomConfig;
    private final UniqueQueue<GameRoomJoinEvent> playersQueue = new UniqueQueue<>();

    public BaseMatchmakerManager(RoomConfig roomConfig) {
        this.roomConfig = roomConfig;
        vertx = Vertx.currentContext().owner();
        eventBus = vertx.eventBus();
        initConsumers();
    }

    protected void initConsumers() {
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL,
                msg -> log.info("Room {} has been created and started", msg.body().getString(Fields.roomId)));
        eventBus.<JsonObject>consumer(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL,
                msg -> log.info("Room {} has been deleted", msg.body().getString(Fields.roomId)));
    }

    @Override
    public void onDisconnect(GatewaySession data) {
        playersQueue.removeIf(e -> e.getSession().getId().equals(data.getId()));
    }

    @Override
    public void onConnect(GatewaySession data) {
    }

    //Default matchmaking implementation
    protected void onJoin(GameRoomJoinEvent joinEvent) {
        playersQueue.add(joinEvent);

        final GatewaySession session = joinEvent.getSession();
        session.send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomConfig.getMaxPlayers())
            return;

        final UUID roomId = UUID.randomUUID();
        final List<GameRoomJoinEvent> userSessions = playersQueue.chunk(roomConfig.getMaxPlayers());

        eventBus.sender(Constants.ROOM_VERTICAL_CHANNEL, new DeliveryOptions()
                        .setLocalOnly(true)
                        .setSendTimeout(1000))
                .write(new JsonObject()
                        .put(Fields.action, ActionType.NEW_ROOM)
                        .put(Fields.roomId, roomId.toString())
                        .put(Fields.sessions, new JsonArray(userSessions.stream().map(e ->
                                        new JsonObject()
                                                .put(Fields.sessionId, e.getSession().getId())
                                                .put(Fields.admin, e.getSession().isAdmin())
                                                .put(Fields.initialData, e.getData()))
                                .collect(Collectors.toList()))))
                .onSuccess(_ -> userSessions.forEach(it -> it.getSession().setRoomKey(roomId)))
                .onFailure(t -> {
                    log.error("Room verticle {} failed to join", roomId, t);
                    userSessions.forEach(it -> it.getSession().close());
                });
    }

    @MessageRoute(MessageTypes.GAME_ROOM_JOIN)
    private void addPlayerToWait(GatewaySession userSession, JsonObject initialData) {
        onJoin(new GameRoomJoinEvent(userSession, initialData));
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
