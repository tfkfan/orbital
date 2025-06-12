package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.InitPack;

public class GameRoomInfoPack implements InitPack {
    private long timestamp;

    public GameRoomInfoPack() {
    }

    public GameRoomInfoPack(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public GameRoomInfoPack setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
