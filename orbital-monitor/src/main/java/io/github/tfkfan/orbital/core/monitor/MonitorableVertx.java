package io.github.tfkfan.orbital.core.monitor;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.micrometer.MicrometerMetricsFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MonitorableVertx {
    public static Future<Vertx> create() {
        return new MonitorableVertx(null).build();
    }

    public static Future<Vertx> create(Consumer<VertxOptions> optionsCustomizer) {
        return new MonitorableVertx(optionsCustomizer).build();
    }

    private final Consumer<VertxOptions> optionsCustomizer;

    public MonitorableVertx(Consumer<VertxOptions> optionsCustomizer) {
        this.optionsCustomizer = optionsCustomizer;
    }

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
        Arrays.stream(meterBinders).forEach(meterBinder -> meterBinder.bindTo(registry));
        var vertxOptions = new VertxOptions()
                .setMetricsOptions(new MicrometerMetricsOptions()
                        .setEnabled(true)
                        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)));

        if (optionsCustomizer != null)
            optionsCustomizer.accept(vertxOptions);

        return Vertx.builder()
                .with(vertxOptions)
                .withMetrics(new MicrometerMetricsFactory(registry)).buildClustered();
    }
}
