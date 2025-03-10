package com.tfkfan.orbital.event.listener;

import com.tfkfan.orbital.event.Event;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.session.Session;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(PlayerSession userSession, E event);
}