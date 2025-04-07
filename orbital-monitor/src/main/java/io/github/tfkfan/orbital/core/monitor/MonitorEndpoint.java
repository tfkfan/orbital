package io.github.tfkfan.orbital.core.monitor;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.micrometer.MetricsService;
import io.vertx.micrometer.PrometheusScrapingHandler;

public interface MonitorEndpoint {
    static void create(Router router) {
        final MetricsService metricsService = MetricsService.create(Vertx.currentContext().owner());

        router.route("/prometheus").handler(PrometheusScrapingHandler.create());
        router.route("/metrics").handler(rc -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .end(metricsService.getMetricsSnapshot().encode())
        );
        router.route("/monitor/*").handler(StaticHandler.create("monitor"));
    }
}
