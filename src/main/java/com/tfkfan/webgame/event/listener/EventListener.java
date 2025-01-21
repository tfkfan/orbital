package com.tfkfan.webgame.event.listener;

import com.tfkfan.webgame.event.Event;
import com.tfkfan.webgame.session.UserSession;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(UserSession userSession, E event);
}