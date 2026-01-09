package io.github.tfkfan.orbital.core.verticle;

import io.github.tfkfan.orbital.core.OrbitalClusterManager;
import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.verticle.impl.GameRoomVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public interface GameVerticle extends Verticle {
    static Future<String> deploy(Vertx vertx, GameVerticle verticle, DeploymentOptions options) {
        return vertx.deployVerticle(() -> verticle, options);
    }

    static Future<String> deploy(Vertx vertx, GameManagerFactory gameManagerFactory, DeploymentOptions options) {
        return vertx.deployVerticle(() -> new GameRoomVerticle(gameManagerFactory), options);
    }

    static String nextVerticleId() {
        return UUID.randomUUID().toString();
    }

    static void startupErrorHandler(Vertx vertx, Throwable e) {
        if (vertx != null)
            vertx.close();
        throw new RuntimeException(e);
    }

    String verticleId();

    DeploymentOptions options();
}
