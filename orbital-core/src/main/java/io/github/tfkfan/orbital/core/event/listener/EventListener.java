package io.github.tfkfan.orbital.core.event.listener;

import io.github.tfkfan.orbital.core.event.Event;
import io.github.tfkfan.orbital.core.session.PlayerSession;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(PlayerSession userSession, E event);
}