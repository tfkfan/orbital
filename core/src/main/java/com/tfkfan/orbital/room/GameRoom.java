package com.tfkfan.orbital.room;

import com.tfkfan.orbital.metrics.GameRoomMetrics;
import com.tfkfan.orbital.scheduler.Scheduler;
import com.tfkfan.orbital.event.Event;
import com.tfkfan.orbital.event.listener.EventListener;
import com.tfkfan.orbital.network.MessageBroadcaster;
import com.tfkfan.orbital.network.RoomEventPublisher;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.state.GameState;

import java.util.Collection;
import java.util.UUID;

public interface GameRoom extends Runnable, MessageBroadcaster, RoomEventPublisher, GameRoomLifecycle, Scheduler {
    static String constructEventListenerConsumer(UUID gameRoomId, Class<?> clazz) {
        return "%s.%s".formatted(gameRoomId, clazz.getSimpleName()).toLowerCase();
    }

    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz);

    GameState state();

    <S extends GameState> S state(Class<S> clazz);

    void update(long dt);

    void onJoin(PlayerSession playerSession);

    void onRejoin(PlayerSession userSession, UUID reconnectKey);

    PlayerSession onDisconnect(PlayerSession userSession);

    Collection<PlayerSession> sessions();

    int players();

    UUID key();

    String verticleID();

    Collection<PlayerSession> close();

    void onClose(PlayerSession userSession);
}

