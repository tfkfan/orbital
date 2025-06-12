package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.*;
import io.github.tfkfan.orbital.core.manager.impl.WebSocketManagerImpl;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.core.route.MessageRoute;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.github.tfkfan.orbital.core.shared.ActionType;
import io.github.tfkfan.orbital.core.shared.UniqueQueue;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BaseMatchmakerManager extends BaseManager implements MatchmakerManager {
    private final Logger log = LoggerFactory.getLogger(BaseMatchmakerManager.class);


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
        final RoomType roomType = RoomType.getOrDefault(joinEvent.getData(), RoomType.BATTLE_ROYALE);

        if (RoomType.TRAINING.equals(roomType)) {
            handleJoinTraining(joinEvent);
            return;
        }

        handleJoin(roomType, joinEvent);
    }

    protected void handleJoinTraining(final GameRoomJoinEvent joinEvent) {
        newRoomEvent(newRoomId(), RoomType.TRAINING, Collections.singletonList(joinEvent));
    }

    protected void handleJoin(final RoomType roomType, final GameRoomJoinEvent joinEvent) {
        playersQueue.add(joinEvent);

        joinEvent.getSession().send(new Message(MessageTypes.GAME_ROOM_JOIN_WAIT));

        if (playersQueue.size() < roomConfig.getMaxPlayers())
            return;

        newRoomEvent(newRoomId(), roomType, playersQueue.chunk(roomConfig.getMaxPlayers()));
    }

    private UUID newRoomId() {
        return UUID.randomUUID();
    }

    protected void newRoomEvent(final UUID roomId, final RoomType roomType, final List<GameRoomJoinEvent> userSessions) {
        eventBus.<JsonObject>request(Constants.ROOM_VERTICAL_CHANNEL, new JsonObject()
                        .put(Fields.action, ActionType.NEW_ROOM)
                        .put(Fields.roomId, roomId.toString())
                        .put(Fields.roomType, roomType.toString())
                        .put(Fields.sessions, new JsonArray(userSessions.stream().map(e ->
                                        new JsonObject()
                                                .put(Fields.sessionId, e.getSession().getId())
                                                .put(Fields.admin, e.getSession().isAdmin())
                                                .put(Fields.initialData, e.getData()))
                                .collect(Collectors.toList()))), new DeliveryOptions()
                        .setLocalOnly(true)
                        .setSendTimeout(1000))
                .onSuccess(message ->
                        log.info("Room successfully created {}", message.body().getString(Fields.roomId))
                )
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
