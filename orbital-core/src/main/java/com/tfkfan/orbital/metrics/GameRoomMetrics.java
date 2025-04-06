package com.tfkfan.orbital.metrics;

public interface GameRoomMetrics extends Measure {
    Integer currentPlayers();
    Integer maxPlayers();
    long alivePlayers();
    long deadPlayers();
}
