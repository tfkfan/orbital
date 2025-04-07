package io.github.tfkfan.orbital.core.verticle;

import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public interface GameVerticle {
    static Future<String> run(Vertx vertx, GatewayVerticle gatewayVerticle, DeploymentOptions options) {
        return vertx.deployVerticle(() -> gatewayVerticle, options);
    }

    static Future<JsonObject> loadConfig(Vertx vertx) {
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(new ConfigStoreOptions()
                        .setType("file")
                        .setFormat("yaml")
                        .setConfig(new JsonObject().put("path", "application.yaml")))
                .addStore(new ConfigStoreOptions()
                        .setType("env"));
        return ConfigRetriever.create(vertx, options).getConfig();
    }

    static String nextVerticleId() {
        return UUID.randomUUID().toString();
    }

    static void startupErrorHandler(Vertx vertx, Throwable e) {
        vertx.close();
        throw new RuntimeException(e);
    }
}
