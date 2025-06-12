package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.Constants;
import io.github.tfkfan.orbital.core.configuration.Fields;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.*;
import io.github.tfkfan.orbital.core.event.listener.EventListener;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.metrics.GameRoomMetrics;
import io.github.tfkfan.orbital.core.metrics.registrar.GameRoomMetricsRegistrar;
import io.github.tfkfan.orbital.core.model.players.Player;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.network.pack.IInitPackProvider;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.network.pack.init.BaseGameInitPack;
import io.github.tfkfan.orbital.core.network.pack.shared.GameRoomInfoPack;
import io.github.tfkfan.orbital.core.network.pack.shared.GameSettingsPack;
import io.github.tfkfan.orbital.core.network.pack.update.BaseGameUpdatePack;
import io.github.tfkfan.orbital.core.scheduler.RoomScheduler;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.session.Session;
import io.github.tfkfan.orbital.core.state.GameState;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.backends.BackendRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

public abstract class AbstractGameRoom<S extends GameState> implements GameRoom {
    private static final Logger log = LoggerFactory.getLogger(AbstractGameRoom.class);

    protected final S state;
    protected final UUID gameRoomId;
    protected final String verticleId;
    protected final Vertx vertx;
    protected final GameManager gameManager;

    private final RoomType roomType;
    private final RoomConfig config;
    private final RoomScheduler scheduler;
    private final Map<String, PlayerSession> sessions = new HashMap<>();
    private final List<MessageConsumer<?>> consumerList = new ArrayList<>();
    private long lastTimestamp = 0L;
    private boolean started = false;

    private final GameRoomMetricsRegistrar gameRoomMetricsRegistrar;

    public AbstractGameRoom(S state, String verticleId, UUID gameRoomId, RoomType roomType, GameManager gameManager, RoomConfig config) {
        this.state = Objects.requireNonNull(state);
        this.gameRoomId = Objects.requireNonNull(gameRoomId);
        this.verticleId = Objects.requireNonNull(verticleId);
        this.roomType = Objects.requireNonNull(roomType);
        this.gameManager = Objects.requireNonNull(gameManager);
        this.config = Objects.requireNonNull(config);
        this.vertx = Vertx.currentContext().owner();
        this.scheduler = new RoomScheduler(vertx);

        addEventListener(this::onPlayerKeyDown, KeyDownPlayerEvent.class);
        addEventListener(this::onPlayerMouseClick, MouseDownPlayerEvent.class);
        addEventListener(this::onPlayerMouseMove, MouseMovePlayerEvent.class);
        addEventListener(this::onPlayerInitRequest, InitPlayerEvent.class, false);

        gameRoomMetricsRegistrar = new GameRoomMetricsRegistrar(BackendRegistries.getDefaultNow(), new GameRoomMetrics() {
            @Override
            public Integer currentPlayers() {
                return state.getPlayers().size();
            }

            @Override
            public Integer maxPlayers() {
                return config.getMaxPlayers();
            }

            @Override
            public long alivePlayers() {
                return state.alivePlayers();
            }

            @Override
            public long deadPlayers() {
                return 0;
            }

            @Override
            public String id() {
                return key().toString();
            }
        });
    }

    @Override
    public RoomType roomType() {
        return roomType;
    }

    @Override
    public RoomConfig config() {
        return config;
    }

    protected void onPlayerKeyDown(PlayerSession playerSession, KeyDownPlayerEvent event) {
    }

    protected void onPlayerMouseClick(PlayerSession playerSession, MouseDownPlayerEvent event) {
    }

    protected void onPlayerMouseMove(PlayerSession playerSession, MouseMovePlayerEvent event) {
    }

    protected void onPlayerInitRequest(PlayerSession playerSession, InitPlayerEvent event) {
        playerSession.send(MessageTypes.INIT,
                new BaseGameInitPack(playerSession.getPlayer().getInitPack(),
                        config.getLoopRate(),
                        state.alivePlayers(),
                        state.getPlayers().stream().map(IInitPackProvider::getInitPack).toList())
        );
    }

    @Override
    public <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz, boolean startCheck) {
        consumerList.add(vertx.eventBus().localConsumer(GameRoom.constructEventListenerConsumer(gameRoomId, clazz), event -> {
            final String playerSessionId = event.headers().get(Fields.sessionId);
            if (!sessions.containsKey(playerSessionId) || startCheck && !started)
                return;
            final PlayerSession playerSession = sessions.get(playerSessionId);
            final E o = event.body() != null ? ((JsonObject) event.body()).mapTo(clazz) : null;
            listener.onEvent(playerSession, o);
        }));
    }

    @Override
    public <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz) {
        addEventListener(listener, clazz, true);
    }

    @Override
    public void onCreate() {
        vertx.eventBus().publish(Constants.MATCHMAKER_ROOM_CREATE_CHANNEL, new JsonObject()
                .put(Fields.roomId, key().toString()));
        gameRoomMetricsRegistrar.register();
        gameManager.onCreate(this);
        log.debug("Room {} has been created", key());
    }

    @Override
    public void onStart() {
        schedule(config.getEndDelay() + config.getStartDelay(), (t) -> onBattleEnd());
        schedule(config.getStartDelay(), (t) -> onBattleStart());
        broadcast(MessageTypes.GAME_ROOM_START, new GameRoomInfoPack(
                OffsetDateTime.now().plus(config.getStartDelay(), ChronoUnit.MILLIS).toInstant().toEpochMilli()
        ));

        gameManager.onStart(this);
        log.debug("Room {} has been started", key());
    }

    @Override
    public void onBattleStart() {
        log.debug("Room {} battle has been started", key());
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
    public void onBattleEnd() {
        log.debug("Room {}. Battle has been ended", key());
        gameManager.onBattleEnd(this);
    }

    @Override
    public void onDestroy() {
        gameRoomMetricsRegistrar.unregister();

        sessions().forEach(playerSession -> state.removePlayer(playerSession.getPlayer()));

        this.sessions.clear();
        consumerList.forEach(MessageConsumer::unregister);
        consumerList.clear();

        scheduler.eraseTasks();

        vertx.eventBus().publish(Constants.MATCHMAKER_ROOM_DESTROY_CHANNEL, new JsonObject()
                .put(Fields.roomId, key().toString()));

        gameManager.onDestroy(this);
    }

    @Override
    public void onJoin(PlayerSession playerSession) {
        this.sessions.put(playerSession.getId(), playerSession);
        playerSession.send(MessageTypes.GAME_ROOM_JOIN_SUCCESS, new GameSettingsPack(config.getLoopRate()));
    }

    @Override
    public void onRejoin(PlayerSession playerSession, UUID reconnectKey) {
        sessions.put(playerSession.getId(), playerSession);
        playerSession.send(MessageTypes.GAME_ROOM_JOIN_SUCCESS, new GameSettingsPack(config.getLoopRate()));
    }

    @Override
    public PlayerSession onDisconnect(PlayerSession playerSession) {
        state.removePlayer(playerSession.getPlayer());
        return sessions.remove(playerSession.getId());
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
    public void broadcast(Function<Session, JsonObject> messageFunction) {
        sessions.values().forEach(session -> session.send(messageFunction.apply(session)));
    }

    @Override
    public void broadcast(JsonObject jsonObject) {
        sessions.values().forEach(session -> session.send(jsonObject));
    }

    @Override
    public void run() {
        try {
            update(lastTimestamp == 0 ? System.currentTimeMillis() : System.currentTimeMillis() - lastTimestamp);
            lastTimestamp = System.currentTimeMillis();
        } catch (Exception e) {
            log.error("room update exception", e);
        }
    }

    @Override
    public void update(long dt) {
        final List<UpdatePack> playerUpdatePackList = updatePlayers(dt);
        sendUpdate(dt, currentPlayer -> new BaseGameUpdatePack(
                currentPlayer.getPrivateUpdatePack(),
                playerUpdatePackList
        ));
    }

    protected List<UpdatePack> updatePlayers(long dt) {
        return state.getPlayers()
                .stream()
                .map(it -> updatePlayer(dt, it))
                .toList();
    }

    protected void sendUpdate(long dt, Function<Player, BaseGameUpdatePack> updatePackFunction) {
        for (var currentPlayer : state.getPlayers())
            currentPlayer.getPlayerSession().send(MessageTypes.UPDATE, updatePackFunction.apply(currentPlayer));
    }

    protected UpdatePack updatePlayer(long dt, Player player) {
        if (player.isAlive())
            player.update(dt);
        return player.getUpdatePack();
    }

    @Override
    public Collection<PlayerSession> close() {
        Collection<PlayerSession> result = new ArrayList<>(sessions.values());
        result.forEach(this::onClose);
        onDestroy();
        log.debug("Room {} has been closed", key());
        return result;
    }

    @Override
    public void onClose(PlayerSession playerSession) {
        playerSession.send(new Message(MessageTypes.GAME_ROOM_CLOSE));
    }

    @Override
    public Collection<PlayerSession> sessions() {
        return sessions.values();
    }

    @Override
    public int players() {
        return sessions().size();
    }

    @Override
    public S state() {
        return state;
    }

    @Override
    public UUID key() {
        return gameRoomId;
    }

    @Override
    public String verticleID() {
        return verticleId;
    }

    @Override
    public void schedule(Long delayMillis, Handler<Long> task) {
        scheduler.schedule(delayMillis, task);
    }

    @Override
    public void schedulePeriodically(Long initDelay, Long loopRate, Handler<Long> task) {
        scheduler.schedulePeriodically(initDelay, loopRate, task);
    }
}
