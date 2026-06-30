package io.github.tfkfan.orbital.core.metrics.registrar;

import io.github.tfkfan.orbital.core.metrics.Measure;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMetricsRegistrar<M extends Measure> implements MetricsRegistrar {
    private final List<Meter> meters = new ArrayList<>();
    private final MeterRegistry registry;
    private final M metrics;

    public AbstractMetricsRegistrar(MeterRegistry registry, M metrics) {
        this.registry = Objects.requireNonNull(registry);
        this.metrics = Objects.requireNonNull(metrics);
    }

    @Override
    public void register() {
        if (!metrics.isMetricsEnabled())
            return;
        registerInternal(metrics);
    }

    protected abstract void registerInternal(M metrics);

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
