package io.github.tfkfan.orbital.core.metrics;

public interface GameRoomMetrics extends Measure {
    Integer currentPlayers();
    Integer maxPlayers();
    long alivePlayers();
    long deadPlayers();
}
