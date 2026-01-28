package io.github.tfkfan.orbital.core.metrics.registrar;

import io.github.tfkfan.orbital.core.metrics.GameManagerMetrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

public class GameManagerMetricsRegistrar extends AbstractMetricsRegistrar {
    private final GameManagerMetrics gameManagerMetrics;

    public GameManagerMetricsRegistrar(MeterRegistry registry, GameManagerMetrics gameManagerMetrics) {
        super(registry);
        this.gameManagerMetrics = gameManagerMetrics;
    }


    @Override
    public void register() {
        register(Gauge.builder("com.tfkfan.orbital.manager.rooms.update.ms", gameManagerMetrics, GameManagerMetrics::avgUpdateMs)
                .description("Avg update ms")
                .tag("id", gameManagerMetrics.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.manager.rooms", gameManagerMetrics, GameManagerMetrics::totalRooms)
                .description("Total rooms count")
                .tag("id", gameManagerMetrics.id())
                .register(registry()));

        register(Gauge.builder("com.tfkfan.orbital.manager.players", gameManagerMetrics, GameManagerMetrics::totalPlayers)
                .description("Total players count")
                .tag("id", gameManagerMetrics.id())
                .register(registry()));
    }
}
