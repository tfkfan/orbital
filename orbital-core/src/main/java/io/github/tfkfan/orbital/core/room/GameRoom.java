package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.scheduler.Scheduler;
import io.github.tfkfan.orbital.core.event.Event;
import io.github.tfkfan.orbital.core.event.listener.EventListener;
import io.github.tfkfan.orbital.core.network.MessageBroadcaster;
import io.github.tfkfan.orbital.core.network.RoomEventPublisher;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;

public interface GameRoom extends Callable<Long>, MessageBroadcaster, RoomEventPublisher, GameRoomLifecycle, Scheduler {
    RoomType roomType();

    RoomConfig config();

    static String constructEventListenerConsumer(UUID gameRoomId, Class<?> clazz) {
        return "%s.%s".formatted(gameRoomId, clazz.getSimpleName()).toLowerCase();
    }

    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz, boolean startCheck);

    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz);

    GameState state();

    void update(long dt);

    void join(PlayerSession playerSession);

    default void onJoin(PlayerSession playerSession) {

    }

    void rejoin(PlayerSession userSession, UUID reconnectKey);

    default void onRejoin(PlayerSession userSession, UUID reconnectKey) {

    }

    PlayerSession disconnect(PlayerSession userSession);

    default void onDisconnect(PlayerSession userSession) {

    }

    Collection<PlayerSession> sessions();

    int players();

    UUID key();

    String verticleID();

    Collection<PlayerSession> close();

    void onClose(PlayerSession userSession);
}

