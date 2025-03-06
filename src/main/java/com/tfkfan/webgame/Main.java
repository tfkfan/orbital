package com.tfkfan.webgame;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.webgame.config.Constants;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
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
        loadConfig(vertx).onSuccess(config -> {
                    vertx.deployVerticle(GatewayVerticle.class, new DeploymentOptions()
                                    .setConfig(new JsonObject().put(Constants.LOCAL_CONFIG, config))
                                    .setWorkerPoolSize(100))
                            .onSuccess(idd -> log.info("Gateway verticle started successfully."))
                            .onFailure(e -> log.error("Gateway verticle deployment failed", e));
                })
                .onFailure(throwable -> log.error("Failed to load config", throwable));
    }

    public static Future<JsonObject> loadConfig(Vertx vertx) {
        final ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "application.yaml"));
        ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
        return ConfigRetriever.create(vertx, options).getConfig();
    }
}
