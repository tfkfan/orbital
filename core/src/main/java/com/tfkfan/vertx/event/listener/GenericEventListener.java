package com.tfkfan.vertx.event.listener;

import com.tfkfan.vertx.event.Event;
import com.tfkfan.vertx.session.UserSession;

public interface GenericEventListener {
    void onEvent(UserSession userSession, Event event);

    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz);
}