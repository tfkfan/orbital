package com.tfkfan.vertx.event.listener;

import com.tfkfan.vertx.event.Event;
import com.tfkfan.vertx.session.UserSession;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(UserSession userSession, E event);
}