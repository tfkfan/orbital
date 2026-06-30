package io.github.tfkfan.orbital.core.metrics.registrar;

import io.github.tfkfan.orbital.core.metrics.GameRoomMetrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class GameRoomMetricsRegistrar extends AbstractMetricsRegistrar<GameRoomMetrics> {
    public GameRoomMetricsRegistrar(MeterRegistry registry, GameRoomMetrics gameRoomMetrics) {
        super(registry, gameRoomMetrics);
    }

    @Override
    public void registerInternal(GameRoomMetrics m) {
        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.active", m, GameRoomMetrics::currentPlayers)
                .description("Current players per room count")
                .tag("id", m.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.max", m, GameRoomMetrics::maxPlayers)
                .description("Max players per room count")
                .tag("id", m.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.alive", m, GameRoomMetrics::alivePlayers)
                .description("Alive players per room count")
                .tag("id", m.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.dead", m, GameRoomMetrics::deadPlayers)
                .description("Dead players per room count")
                .tag("id", m.id())
                .register(registry()));
    }
}
