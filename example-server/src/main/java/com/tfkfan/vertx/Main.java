package com.tfkfan.vertx;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.verticle.DefaultGatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        BaseVerticle.loadConfig(vertx).onSuccess(config -> vertx.deployVerticle(DefaultGatewayVerticle.class, new DeploymentOptions()
                                .setConfig(new JsonObject().put(Constants.LOCAL_CONFIG, config)))
                        .onSuccess(_ -> log.info("Gateway verticle started successfully."))
                        .onFailure(e -> log.error("Gateway verticle deployment failed", e)))
                .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
    }
}
