package com.tfkfan.webgame.config;

public interface Constants {
    String ROOMS_VERTICAL_CREATE_CHANNEL = "room.vertical.create";
    String ROOMS_VERTICAL_DESTROY_CHANNEL = "room.vertical.destroy";
    String ROOM_VERTICAL_CHANNEL = "room.vertical.";
    String WS_CHANNEL = "ws";
    String WS_SESSION_CHANNEL = "ws.";
    String MATCHMAKER_ROOM_CREATE_CHANNEL = "matchmaker.room.create";
    String MATCHMAKER_ROOM_DESTROY_CHANNEL = "matchmaker.room.destroy";

    String LOCAL_CONFIG = "localConfig";
    String ROOM_VERTICAL_ID = "roomVerticalId";
    String WEBSOCKET_PATH = "/websocket";
    Double ABS_ANGULAR_VEL_RAD = 0.05;
    Double ABS_ANGULAR_VEL_DEG = 2.8;
    Double ANGULAR_TRESHOLD = ABS_ANGULAR_VEL_RAD;
    Double ABS_SKILL_SPEED = 15.0;
    Double ABS_PLAYER_SPEED = 5.0;
    Double ABS_PLAYER_ACC = 2.0;
    Double ABS_SIGHT_RADIUS = 500.0;
    Double ABS_RENTGEN_RADIUS = 100.0;
    Double SIGHT_ANGLE = 30.0;
    Integer SIGHT_OFFSET = 3;
    Integer CELLS_WIDTH = 100;
    Integer MAX_THREADS = 4;
    Integer CELLS_HEIGHT = 100;
    Integer CELLS_OFFSET = 1;
    Double DR = 1.5;
    Double STRIKE_DISTANCE_LIMIT = 1000.0;
    Long DEFAULT_COOLDOWN = 300L;
    Long DEFAULT_LOOPRATE = 300L;
    Long MAX_SKILL_DURATION = 10000L;
    Long ROOM_START_DELAY = 30000L;
    Long LOOT_DELAY = 30000L;
    Long ROOM_END_DELAY = 1000L * 60 * 5;
    Long ROOM_INIT_DELAY = 5000L;
    Long MAX_PLAYERS = 20L;
}
