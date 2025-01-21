package com.tfkfan.webgame;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.webgame.config.Constants;
import com.tfkfan.webgame.properties.ApplicationProperties;
import com.tfkfan.webgame.shared.VertxUtils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        init();
        final Vertx vertx = Vertx.vertx();
        VertxUtils.loadConfig(vertx).onSuccess(config -> {
                    vertx.deployVerticle(GatewayVerticle.class, new DeploymentOptions()
                                    .setConfig(new JsonObject().put(Constants.LOCAL_CONFIG, config))
                                    .setWorkerPoolSize(100))
                            .onSuccess(idd -> {
                                log.info("Gateway verticle started successfully.");
                            })
                            .onFailure(e -> log.error("Gateway verticle deployment failed", e));
                })
                .onFailure(throwable -> log.error("Failed to load config", throwable));
    }

    private static void init() {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
