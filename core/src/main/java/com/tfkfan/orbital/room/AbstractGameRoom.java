package com.tfkfan.orbital.room;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.*;
import com.tfkfan.orbital.event.listener.EventListener;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.model.players.Player;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.network.message.MessageType;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.network.pack.init.GameInitPack;
import com.tfkfan.orbital.network.pack.IInitPackProvider;
import com.tfkfan.orbital.network.pack.shared.GameRoomInfoPack;
import com.tfkfan.orbital.network.pack.shared.GameSettingsPack;
import com.tfkfan.orbital.network.pack.update.GameUpdatePack;
import com.tfkfan.orbital.session.UserSession;
import com.tfkfan.orbital.state.GameState;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Slf4j
public abstract class AbstractGameRoom implements GameRoom {
    protected boolean started = false;
    protected final GameState state;
    protected final UUID gameRoomId;
    protected final String verticleId;
    protected final Vertx vertx;
    protected final GameManager gameManager;
    private final List<Long> roomFutureList = new ArrayList<>();
    private final Map<String, UserSession> sessions = new HashMap<>();
    private final List<MessageConsumer<?>> consumerList = new ArrayList<>();

    protected final RoomConfig config;

    public AbstractGameRoom(GameState state, String verticleId, UUID gameRoomId, GameManager gameManager, RoomConfig config) {
        this.state = state;
        this.gameRoomId = gameRoomId;
        this.verticleId = verticleId;
        this.gameManager = gameManager;
        this.config = config;
        this.vertx = Vertx.currentContext().owner();

        addEventListener(this::onPlayerKeyDown, KeyDownPlayerEvent.class);
        addEventListener(this::onPlayerMouseClick, MouseDownPlayerEvent.class);
        addEventListener(this::onPlayerMouseMove, MouseMovePlayerEvent.class);
        addEventListener(this::onPlayerInitRequest, InitPlayerEvent.class);
    }

    protected void onPlayerKeyDown(UserSession userSession, KeyDownPlayerEvent event) {
    }

    protected void onPlayerMouseClick(UserSession userSession, MouseDownPlayerEvent event) {
    }

    protected void onPlayerMouseMove(UserSession userSession, MouseMovePlayerEvent event) {
    }

    protected void onPlayerInitRequest(UserSession userSession, InitPlayerEvent event) {
        userSession.send(MessageTypes.INIT,
                new GameInitPack(userSession.getPlayer().getInitPack(),
                        config.getLoopRate(),
                        state.alivePlayers(),
                        state.getPlayers().stream().map(IInitPackProvider::getInitPack).toList())
        );
    }

    @Override
    public <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz) {
        consumerList.add(vertx.eventBus().localConsumer(GameRoom.constructEventListenerConsumer(gameRoomId, clazz), event -> {
            final String userSessionId = event.headers().get(Fields.sessionId);
            if (!sessions.containsKey(userSessionId))
                return;
            final UserSession userSession = sessions.get(userSessionId);
            final E o = event.body() != null ? ((JsonObject) event.body()).mapTo(clazz) : null;
            listener.onEvent(userSession, o);
        }));
    }

    @Override
    public void onRoomCreated(List<UserSession> userSessions) {
        for (var userSession : userSessions) {
            this.sessions.put(userSession.getId(), userSession);
            broadcast(MessageTypes.MESSAGE, "%s successfully joined".formatted(userSession.getPlayer().getId().toString()));
        }

        vertx.eventBus().publish(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL, new JsonObject()
                .put(Fields.roomId, key().toString()));

        broadcast(_ -> new JsonObject()
                .put(Fields.type, MessageTypes.GAME_ROOM_JOIN_SUCCESS)
                .put(Fields.data, JsonObject.mapFrom(new GameSettingsPack(config.getLoopRate())))
        );

        log.trace("Room {} has been created", key());
    }

    @Override
    public void onRoomStarted() {
        schedule(config.getEndDelay() + config.getStartDelay(), (_) -> gameManager.onBattleEnd(this));
        schedule(config.getStartDelay(), this::onBattleStarted);
        broadcast(MessageTypes.GAME_ROOM_START, new GameRoomInfoPack(
                OffsetDateTime.now().plus(config.getStartDelay(), ChronoUnit.MILLIS).toInstant().toEpochMilli()
        ));
        log.trace("Room {} has been started", key());
    }

    @Override
    public void onBattleStarted(long timerId) {
        log.trace("Room {}. Battle has been started", key());
        started = true;
        gameManager.onBattleStart(this);
        schedulePeriodically(config.getInitDelay(), config.getLoopRate(), this::update);
        broadcast(MessageTypes.GAME_ROOM_BATTLE_START, new GameRoomInfoPack(
                OffsetDateTime.now()
                        .plus(config.getEndDelay(), ChronoUnit.MILLIS)
                        .toInstant()
                        .toEpochMilli()
        ));
    }

    @Override
    public void onDestroy() {
        sessions().forEach(userSession -> state.removePlayer(userSession.getPlayer()));

        this.sessions.clear();
        consumerList.forEach(MessageConsumer::unregister);
        consumerList.clear();
        roomFutureList.forEach(vertx::cancelTimer);
        roomFutureList.clear();

        vertx.eventBus().publish(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL, new JsonObject()
                .put(Fields.roomId, key().toString()));
    }

    @Override
    public void onRejoin(UserSession userSession, UUID reconnectKey) {
        userSession.setRoomKey(key());
        sessions.put(userSession.getId(), userSession);

        userSession.send(MessageTypes.GAME_ROOM_JOIN_SUCCESS, new GameSettingsPack(config.getLoopRate()));
    }

    @Override
    public UserSession onDisconnect(UserSession userSession) {
        return sessions.remove(userSession.getId());
    }

    @Override
    public void broadcast(MessageType type, String message) {
        sessions.values().forEach(session -> session.sendText(message));
    }

    @Override
    public void broadcast(Message message) {
        sessions.values().forEach(session -> session.send(message));
    }

    @Override
    public void broadcast(int messageType, Object content) {
        sessions.values().forEach(session -> session.send(messageType, content));
    }

    @Override
    public void broadcast(Function<UserSession, JsonObject> messageFunction) {
        sessions.values().forEach(session -> session.send(messageFunction.apply(session)));
    }

    @Override
    public void run() {
        try {
            update(0);
        } catch (Exception e) {
            log.error("room update exception", e);
        }
    }

    @Override
    public void update(long timerID) {
        List<UpdatePack> playerUpdatePackList = state.getPlayers()
                .stream()
                .map(this::updatePlayer)
                .toList();

        for (var currentPlayer : state.getPlayers())
            currentPlayer.getUserSession().send(MessageTypes.UPDATE, new GameUpdatePack(
                    currentPlayer.getPrivateUpdatePack(),
                    playerUpdatePackList
            ));
    }

    protected UpdatePack updatePlayer(Player player) {
        if (player.isAlive())
            player.update();
        return player.getUpdatePack();
    }

    @Override
    public Collection<UserSession> close() {
        Collection<UserSession> result = sessions.values();
        result.forEach(this::onClose);
        onDestroy();
        log.trace("Room {} has been closed", key());
        return result;
    }

    @Override
    public void onClose(UserSession userSession) {
        userSession.send(new Message(MessageTypes.GAME_ROOM_CLOSE));
    }

    @Override
    public Optional<UserSession> getPlayerSessionBySessionId(UserSession userSession) {
        return Optional.ofNullable(sessions.get(userSession.getId()));
    }

    @Override
    public Collection<UserSession> sessions() {
        return sessions.values();
    }

    @Override
    public int currentPlayersCount() {
        return sessions().size();
    }

    @Override
    public GameState state() {
        return state;
    }

    @Override
    public <S extends GameState> S state(Class<S> clazz) {
        return clazz.cast(state);
    }

    @Override
    public UUID key() {
        return gameRoomId;
    }

    @Override
    public String verticleId() {
        return verticleId;
    }

    @Override
    public void schedule(Long delayMillis, Handler<Long> task) {
        if (delayMillis <= 1) {
            task.handle(0L);
            return;
        }

        roomFutureList.add(vertx.setTimer(delayMillis, (t) -> {
            task.handle(t);
            roomFutureList.remove(t);
        }));
    }

    @Override
    public void schedulePeriodically(Long initDelay, Long loopRate, Handler<Long> task) {
        roomFutureList.add(vertx.setPeriodic(initDelay, loopRate, task));
    }
}
