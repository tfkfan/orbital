package com.tfkfan.orbital.room;

import com.tfkfan.orbital.event.Event;
import com.tfkfan.orbital.event.listener.EventListener;
import com.tfkfan.orbital.network.MessageBroadcaster;
import com.tfkfan.orbital.network.RoomEventPublisher;
import com.tfkfan.orbital.session.UserSession;
import com.tfkfan.orbital.state.GameState;
import io.vertx.core.Handler;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRoom extends Runnable, MessageBroadcaster, RoomEventPublisher {
    static String constructEventListenerConsumer(UUID gameRoomId, Class<?> clazz) {
        return "%s.%s".formatted(gameRoomId, clazz.getSimpleName()).toLowerCase();
    }
    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz);

    GameState state();

    <S extends GameState> S state(Class<S> clazz);

    void update(long timerID);

    void onRoomCreated(List<UserSession> userSessions);

    void onRoomStarted();

    void onBattleStarted(long timerId);

    void onDestroy();

    void onRejoin(UserSession userSession, UUID reconnectKey);

    UserSession onDisconnect(UserSession userSession);

    Collection<UserSession> sessions();

    int currentPlayersCount();

    Optional<UserSession> getPlayerSessionBySessionId(UserSession userSession);

    UUID key();

    String verticleId();

    Collection<UserSession> close();

    void onClose(UserSession userSession);

    void schedule(Long delayMillis, Handler<Long> task);

    void schedulePeriodically(Long initDelay, Long loopRate, Handler<Long> task);
}

