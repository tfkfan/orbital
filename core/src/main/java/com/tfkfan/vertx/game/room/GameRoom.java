package com.tfkfan.vertx.game.room;

import com.tfkfan.vertx.event.listener.GenericEventListener;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.network.SocketMessageBroadcaster;
import com.tfkfan.vertx.session.UserSession;
import io.vertx.core.Handler;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRoom extends Runnable, SocketMessageBroadcaster, GenericEventListener {
    static String constructEventListenerConsumer(UUID gameRoomId, Class<?> clazz) {
        return "%s.%s".formatted(gameRoomId, clazz.getSimpleName()).toLowerCase();
    }

    GameMap gameMap();

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

