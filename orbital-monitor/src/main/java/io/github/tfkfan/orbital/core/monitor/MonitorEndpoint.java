package io.github.tfkfan.orbital.core.monitor;

import io.github.tfkfan.orbital.core.Orbital;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.micrometer.MetricsService;
import io.vertx.micrometer.PrometheusScrapingHandler;

public class MonitorEndpoint {
    private final CorsHandler corsHandler;

    public MonitorEndpoint() {
        this(null);
    }

    public MonitorEndpoint(CorsHandler corsHandler) {
        this.corsHandler = corsHandler;
    }

    protected void wrapCall(RoutingContext context, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            context.fail(throwable);
        }
    }

    public void create(Router router) {
        if (corsHandler != null)
            router.route().handler(corsHandler);
        final MetricsService metricsService = MetricsService.create(Vertx.currentContext().owner());
        router.route("/cluster/list").handler(rc ->
                Orbital.get().getOrbitalManager().getGatewayInfoList().onComplete(result -> {
                    wrapCall(rc, () -> {
                        if (result.succeeded())
                            rc.response().end(new JsonArray(result.result()).encode());
                        else
                            rc.response().end();
                    });
                }));
        router.route("/prometheus").handler(PrometheusScrapingHandler.create());
        router.route("/metrics").handler(rc -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .end(metricsService.getMetricsSnapshot().encode()));
        router.route("/monitor/*").handler(StaticHandler.create("monitor"));
    }
}
