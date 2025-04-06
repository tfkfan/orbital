package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.metrics.GameManagerMetrics;
import com.tfkfan.orbital.room.GameRoomLifecycle;
import io.vertx.core.metrics.Measured;

public interface GameManager extends GameRoomLifecycle.GameRoomLifecycleHandler, Measured, GameManagerMetrics {
    @Override
    default boolean isMetricsEnabled() {
        return true;
    }
}
