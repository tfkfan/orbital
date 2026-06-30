package io.github.tfkfan.orbital.core.manager;

import io.github.tfkfan.orbital.core.metrics.GameManagerMetrics;
import io.github.tfkfan.orbital.core.room.GameRoomLifecycle;
import io.github.tfkfan.orbital.core.room.GameRoomManagerLifecycle;

public interface GameRoomManager extends GameRoomManagerLifecycle, GameManagerMetrics, Manager {
}