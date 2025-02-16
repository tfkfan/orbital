package com.tfkfan.vertx.game.room;

import com.tfkfan.vertx.configuration.Fields;
import com.tfkfan.vertx.configuration.MessageTypes;
import com.tfkfan.vertx.event.Event;
import com.tfkfan.vertx.event.listener.EventListener;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.network.message.Message;
import com.tfkfan.vertx.network.message.MessageType;
import com.tfkfan.vertx.session.UserSession;
import com.tfkfan.vertx.shared.RoomUtils;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

@Slf4j
public abstract class AbstractGameRoom implements GameRoom {
    protected final UUID gameRoomId;
    protected final String verticleId;
    protected final Vertx vertx;
    protected final GameManager gameManager;
    private final List<Long> roomFutureList = new ArrayList<>();
    private final Map<String, UserSession> sessions = new HashMap<>();
    private final List<MessageConsumer<?>> consumerList = new ArrayList<>();

    public AbstractGameRoom(String verticleId, UUID gameRoomId, GameManager gameManager) {
        this.gameRoomId = gameRoomId;
        this.verticleId = verticleId;
        this.gameManager = gameManager;
        this.vertx = Vertx.currentContext().owner();
    }

    @Override
    public <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz) {
        consumerList.add(vertx.eventBus().consumer(RoomUtils.constructEventListenerConsumer(gameRoomId, clazz), event -> {
            final String userSessionId = event.headers().get(Fields.sessionId);
            if (!sessions.containsKey(userSessionId))
                return;
            final UserSession userSession = sessions.get(userSessionId);
            final E o = event.body() != null ? ((JsonObject) event.body()).mapTo(clazz) : null;
            listener.onEvent(userSession, o);
        }));
    }

    @Override
    public void onEvent(UserSession userSession, Event event) {
        vertx.eventBus().publish(RoomUtils.constructEventListenerConsumer(gameRoomId, event.getClass()),
                JsonObject.mapFrom(event), new DeliveryOptions().addHeader(Fields.sessionId, userSession.getId()));
    }

    @Override
    public void onRoomCreated(List<UserSession> userSessions) {
        for (var userSession : userSessions) {
            this.sessions.put(userSession.getId(), userSession);
            broadcast(MessageTypes.MESSAGE, "%s successfully joined".formatted(userSession.getPlayer().getId().toString()));
        }
    }

    @Override
    public void onDestroy() {
        this.sessions.clear();
        consumerList.forEach(MessageConsumer::unregister);
        roomFutureList.forEach(vertx::cancelTimer);
        roomFutureList.clear();
    }

    @Override
    public void onRejoin(UserSession userSession, UUID reconnectKey) {
        userSession.setRoomKey(key());
        sessions.put(userSession.getId(), userSession);
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
    public Collection<UserSession> close() {
        Collection<UserSession> result = sessions.values();
        result.forEach(this::onClose);
        onDestroy();
        log.trace("Room {} has been closed", key());
        return result;
    }

    @Override
    public void onClose(UserSession userSession) {
    }

    @Override
    public Optional<UserSession> getPlayerSessionBySessionId(UserSession userSession) {
        if (sessions.containsKey(userSession.getId())) return Optional.of(sessions.get(userSession.getId()));
        return Optional.empty();
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
