package com.tfkfan.orbital.configuration;

public interface Constants {
    String ROOM_VERTICAL_CHANNEL = "room.vertical.";
    String WS_CHANNEL = "ws";
    String WS_SESSION_CHANNEL = "ws.";
    String MATCHMAKER_ROOM_CREATE_CHANNEL = "matchmaker.room.create";
    String MATCHMAKER_ROOM_DESTROY_CHANNEL = "matchmaker.room.destroy";
    String WEBSOCKET_PATH = "/websocket";
    Double ABS_PLAYER_SPEED = 5.0;
    Long DEFAULT_LOOPRATE = 300L;
    Long ROOM_START_DELAY = 30000L;
    Long ROOM_END_DELAY = 1000L * 60 * 5;
    Long ROOM_INIT_DELAY = 5000L;
    Long MAX_PLAYERS = 20L;
}
