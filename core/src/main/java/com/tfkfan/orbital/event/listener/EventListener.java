package com.tfkfan.orbital.event.listener;

import com.tfkfan.orbital.event.Event;
import com.tfkfan.orbital.session.UserSession;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(UserSession userSession, E event);
}