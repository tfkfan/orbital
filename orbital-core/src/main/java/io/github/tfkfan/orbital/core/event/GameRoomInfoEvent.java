package io.github.tfkfan.orbital.core.event;

public class GameRoomInfoEvent extends AbstractEvent{
    private String reconnectKey;

    public GameRoomInfoEvent() {
    }

    public GameRoomInfoEvent(String reconnectKey) {
        this.reconnectKey = reconnectKey;
    }

    public String getReconnectKey() {
        return reconnectKey;
    }

    public GameRoomInfoEvent setReconnectKey(String reconnectKey) {
        this.reconnectKey = reconnectKey;
        return this;
    }
}
