package io.github.tfkfan.orbital.core.metrics.registrar;

import io.github.tfkfan.orbital.core.metrics.GameRoomMetrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class GameRoomMetricsRegistrar extends AbstractMetricsRegistrar {
    private final GameRoomMetrics gameRoomMetrics;

    public GameRoomMetricsRegistrar(MeterRegistry registry, GameRoomMetrics gameRoomMetrics) {
        super(registry);
        this.gameRoomMetrics = gameRoomMetrics;
    }

    @Override
    public void register() {
        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.active", gameRoomMetrics, GameRoomMetrics::currentPlayers)
                .description("Current players per room count")
                .tag("id", gameRoomMetrics.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.max", gameRoomMetrics, GameRoomMetrics::maxPlayers)
                .description("Max players per room count")
                .tag("id", gameRoomMetrics.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.alive", gameRoomMetrics, GameRoomMetrics::alivePlayers)
                .description("Alive players per room count")
                .tag("id", gameRoomMetrics.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.game.rooms.players.dead", gameRoomMetrics, GameRoomMetrics::deadPlayers)
                .description("Dead players per room count")
                .tag("id", gameRoomMetrics.id())
                .register(registry()));
    }
}
