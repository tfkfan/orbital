package com.tfkfan.orbital.monitor;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

import java.util.Arrays;

public final class MonitorableVertx {
    public Vertx build() {
        return build(new PrometheusRegistryBuilder().build());
    }

    public Vertx build(PrometheusMeterRegistry registry) {
        return build(registry,
                new JvmGcMetrics(),
                new JvmHeapPressureMetrics(),
                new UptimeMetrics(),
                new ClassLoaderMetrics(),
                new JvmMemoryMetrics(),
                new ProcessorMetrics(),
                new JvmThreadMetrics(),
                new JvmInfoMetrics());
    }

    public Vertx build(PrometheusMeterRegistry registry, MeterBinder... meterBinders) {
        Arrays.stream(meterBinders).forEach(meterBinder -> meterBinder.bindTo(registry));
        return Vertx.builder().with(new VertxOptions().setMetricsOptions(new MicrometerMetricsOptions()
                        .setEnabled(true)
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))))
                .withMetrics(new MicrometerMetricsFactory(registry)).build();
    }
}
