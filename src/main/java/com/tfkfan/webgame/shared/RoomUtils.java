package com.tfkfan.webgame.shared;

import java.util.UUID;

public class RoomUtils {
    public static String constructEventListenerConsumer(UUID gameRoomId, Class<?> clazz) {
        return "%s.%s".formatted(gameRoomId, clazz.getSimpleName()).toLowerCase();
    }
}
