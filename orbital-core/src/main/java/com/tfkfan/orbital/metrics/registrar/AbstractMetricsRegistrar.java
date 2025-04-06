package com.tfkfan.orbital.metrics.registrar;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMetricsRegistrar implements MetricsRegistrar {
    private final List<Meter> meters = new ArrayList<>();
    private final MeterRegistry registry;

    public AbstractMetricsRegistrar(MeterRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    @Override
    public void register(Meter meter) {
        meters.add(meter);
    }

    @Override
    public void unregister() {
        meters.forEach(it -> registry().remove(it));
    }

    public MeterRegistry registry() {
        return registry;
    }
}
