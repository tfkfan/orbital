package com.tfkfan.orbital;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.orbital.verticle.DefaultWebsocketGatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        BaseVerticle.loadConfig(vertx).onSuccess(config -> vertx.deployVerticle(DefaultWebsocketGatewayVerticle.class, new DeploymentOptions()
                                .setConfig(config))
                        .onSuccess(_ -> log.info("Gateway verticle started successfully."))
                        .onFailure(e -> log.error("Gateway verticle deployment failed", e)))
                .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
    }
}
