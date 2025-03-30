package com.tfkfan.orbital.monitor;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.micrometer.PrometheusScrapingHandler;

public interface MonitorEndpoint {
    static void create(Router router) {
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
        router.route("/monitor/*").handler(StaticHandler.create("monitor"));
    }
}
