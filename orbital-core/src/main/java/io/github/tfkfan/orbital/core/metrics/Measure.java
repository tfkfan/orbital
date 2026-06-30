package io.github.tfkfan.orbital.core.metrics;

import io.vertx.core.metrics.Measured;

public interface Measure extends Measured {
    String id();

    @Override
    default boolean isMetricsEnabled() {
        return true;
    }
}
