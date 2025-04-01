package com.tfkfan.orbital.metrics.registrar;

import io.micrometer.core.instrument.Meter;

public interface MetricsRegistrar {
    void register(Meter meter);

    void register();

    default void unregister() {

    }
}
