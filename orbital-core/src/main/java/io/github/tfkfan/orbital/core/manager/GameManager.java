package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.metrics.GameManagerMetrics;
import io.github.tfkfan.orbital.core.room.GameRoomLifecycle;
import io.vertx.core.metrics.Measured;

public interface GameManager extends GameRoomLifecycle.GameRoomLifecycleHandler, Measured, GameManagerMetrics {
    @Override
    default boolean isMetricsEnabled() {
        return true;
    }
}
