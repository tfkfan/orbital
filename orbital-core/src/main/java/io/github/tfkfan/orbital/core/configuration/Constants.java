package io.github.tfkfan.orbital.core.configuration;

public interface Constants {
    String GATEWAY_ROOM_CREATE_CHANNEL = "gateway.room.create";
    String GATEWAY_ROOM_DESTROY_CHANNEL = "gateway.room.destroy";
    String ROOM_VERTICAL_CHANNEL = "room.vertical.";
    String WS_CHANNEL = ".";
    String WS_SESSION_CHANNEL = ".s.";
    String MATCHMAKER_ROOM_CREATE_CHANNEL = "matchmaker.room.create";
    String MATCHMAKER_ROOM_DESTROY_CHANNEL = "matchmaker.room.destroy";
    String GAME_ADDRESS = "game";
    String ADMIN_ADDRESS = "admin";
    String WS_GAME_PATH = "/game";
    String WS_ADMIN_PATH = "/admin";
    String ROOM_VERTICAL_ID = "room.vertical.id";

    Long DEFAULT_LOOPRATE = 300L;
    Long ROOM_START_DELAY = 30000L;
    Long ROOM_END_DELAY = 1000L * 60 * 5;
    Long ROOM_INIT_DELAY = 5000L;
    Integer MAX_PLAYERS = 20;

    static String sessionConsumer(String address, String id) {
        return address + Constants.WS_SESSION_CHANNEL + id;
    }

    static String broadcastConsumer(String address) {
        return address + Constants.WS_CHANNEL;
    }
}
