package com.tfkfan.orbital.monitor;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class PrometheusRegistryBuilder {
    private MeterFilter meterFilter;

    public PrometheusRegistryBuilder withMeterFilter(MeterFilter meterFilter) {
        this.meterFilter = meterFilter;
        return this;
    }

    public PrometheusMeterRegistry build() {
        final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        if (meterFilter == null)
            meterFilter = new MeterFilter() {
                @Override
                public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                    return DistributionStatisticConfig.builder().percentilesHistogram(true).build().merge(config);
                }
            };
        registry.config().meterFilter(meterFilter);

        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        try (var m = new JvmGcMetrics()) {
            m.bindTo(registry);
        }
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        return registry;
    }
}
