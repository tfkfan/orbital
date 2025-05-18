package io.github.tfkfan.orbital.core.monitor;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.spi.VertxMetricsFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.micrometer.MicrometerMetricsFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

import java.util.Arrays;

public final class MonitorableClusteredVertx {
    public Future<Vertx> build() {
        return build(new PrometheusRegistryBuilder().build());
    }

    public Future<Vertx> build(PrometheusMeterRegistry registry) {
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

    public Future<Vertx> build(PrometheusMeterRegistry registry, MeterBinder... meterBinders) {
        return build(new VertxPrometheusOptions().setEnabled(true), registry, meterBinders);
    }

    public Future<Vertx> build(VertxPrometheusOptions prometheusOptions, PrometheusMeterRegistry registry, MeterBinder... meterBinders) {
        Arrays.stream(meterBinders).forEach(meterBinder -> meterBinder.bindTo(registry));
        return build(new MicrometerMetricsOptions()
                .setFactory(new MicrometerMetricsFactory(registry))
                .setEnabled(true)
                .setPrometheusOptions(prometheusOptions));
    }

    public Future<Vertx> build(MetricsOptions metricsOptions) {
        return build(new VertxOptions().setMetricsOptions(metricsOptions));
    }


    public Future<Vertx> build(VertxOptions options,
                               VertxMetricsFactory metricsFactory,
                               ClusterManager clusterManager) {
        return Vertx.builder()
                .with(options)
                .withMetrics(metricsFactory)
                .withClusterManager(clusterManager)
                .buildClustered();
    }
}
