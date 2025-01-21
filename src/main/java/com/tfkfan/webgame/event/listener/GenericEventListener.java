package com.tfkfan.webgame.event.listener;

import com.tfkfan.webgame.event.Event;
import com.tfkfan.webgame.session.UserSession;

public interface GenericEventListener {
    void onEvent(UserSession userSession, Event event);

    <E extends Event> void addEventListener(EventListener<E> listener, Class<E> clazz);
}