package io.github.tfkfan.orbital.core.configuration;

public interface Constants {
    String ROOM_VERTICAL_CHANNEL = "room.vertical.";
    String WS_CHANNEL = ".";
    String WS_SESSION_CHANNEL = ".s.";
    String MATCHMAKER_ROOM_CREATE_CHANNEL = "matchmaker.room.create";
    String MATCHMAKER_ROOM_DESTROY_CHANNEL = "matchmaker.room.destroy";
    String GAME_ADDR_PREFIX = "game";
    String ADMIN_ADDR_PREFIX = "admin";
    String WS_GAME_PATH = "/game";
    String WS_ADMIN_PATH = "/admin";

    Long DEFAULT_LOOPRATE = 300L;
}
