package io.github.tfkfan.orbital.core.metrics.registrar;

import io.github.tfkfan.orbital.core.metrics.GameManagerMetrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class GameManagerMetricsRegistrar extends AbstractMetricsRegistrar<GameManagerMetrics> {
    public GameManagerMetricsRegistrar(MeterRegistry registry, GameManagerMetrics gameManagerMetrics) {
        super(registry, gameManagerMetrics);
    }

    @Override
    public void registerInternal(GameManagerMetrics m) {
        register(Gauge.builder("com.tfkfan.orbital.manager.rooms", m, GameManagerMetrics::totalRooms)
                .description("Total rooms count")
                .tag("id", m.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.manager.players", m, GameManagerMetrics::totalPlayers)
                .description("Total players count")
                .tag("id", m.id())
                .register(registry()));
    }
}
