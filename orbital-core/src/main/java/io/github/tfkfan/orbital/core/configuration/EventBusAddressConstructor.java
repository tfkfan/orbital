package io.github.tfkfan.orbital.core.configuration;

public final class EventBusAddressConstructor {
    private EventBusAddressConstructor() {
    }

    public static String sessionConsumer(String id) {
        return sessionConsumer(Constants.GAME_ADDR_PREFIX, id);
    }

    public static String sessionConsumer(String address, String id) {
        return address + Constants.WS_SESSION_CHANNEL + id;
    }

    public static String broadcastConsumer(String address) {
        return address + Constants.WS_CHANNEL;
    }
}
