package com.tfkfan.orbital.configuration;

public interface Constants {
    String GATEWAY_ROOM_CREATE_CHANNEL = "gateway.room.create";
    String GATEWAY_ROOM_DESTROY_CHANNEL = "gateway.room.destroy";
    String ROOM_VERTICAL_CHANNEL = "room.vertical.";
    String WS_CHANNEL = "ws";
    String WS_SESSION_CHANNEL = "ws.";
    String MATCHMAKER_ROOM_CREATE_CHANNEL = "matchmaker.room.create";
    String MATCHMAKER_ROOM_DESTROY_CHANNEL = "matchmaker.room.destroy";
    String WEBSOCKET_PATH = "/websocket";
    String ROOM_VERTICAL_ID = "room.vertical.id";
    Double ABS_PLAYER_SPEED = 5.0;
    Long DEFAULT_LOOPRATE = 300L;
    Long ROOM_START_DELAY = 30000L;
    Long ROOM_END_DELAY = 1000L * 60 * 5;
    Long ROOM_INIT_DELAY = 5000L;
    Integer MAX_PLAYERS = 20;
}
