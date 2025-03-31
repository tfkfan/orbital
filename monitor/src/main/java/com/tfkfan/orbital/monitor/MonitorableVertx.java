package com.tfkfan.orbital.monitor;

import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

public final class MonitorableVertx {
    public Vertx build() {
        return build(new PrometheusRegistryBuilder().build());
    }

    public Vertx build(PrometheusMeterRegistry registry) {
        new JvmGcMetrics().bindTo(registry);
        new JvmHeapPressureMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        new JvmInfoMetrics().bindTo(registry);
        return Vertx.builder().with(new VertxOptions().setMetricsOptions(new MicrometerMetricsOptions()
                        .setEnabled(true)
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))))
                .withMetrics(new MicrometerMetricsFactory(registry)).build();
    }
}
