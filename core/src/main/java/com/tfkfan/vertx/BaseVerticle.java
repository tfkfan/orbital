package com.tfkfan.vertx;

import com.tfkfan.vertx.manager.StopListener;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseVerticle extends AbstractVerticle {
    protected String verticleId;
    private StopListener stopListener;

    public BaseVerticle() {
    }

    public BaseVerticle(String verticleId) {
        this.verticleId = Objects.requireNonNull(verticleId);
    }

    protected BaseVerticle stopListener(StopListener stopListener) {
        this.stopListener = stopListener;
        return this;
    }

    protected BaseVerticle verticleId(String verticleId) {
        this.verticleId = Objects.requireNonNull(verticleId);
        return this;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        if (verticleId == null)
            verticleId = nextVerticleId();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
        if (stopListener != null)
            stopListener.stop(stopPromise);
    }

    protected String nextVerticleId() {
        return UUID.randomUUID().toString();
    }

    public static void startupErrorHandler(Vertx vertx, Throwable e) {
        vertx.close();
        throw new RuntimeException(e);
    }

    public static Future<JsonObject> loadConfig(Vertx vertx) {
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(new ConfigStoreOptions()
                        .setType("file")
                        .setFormat("yaml")
                        .setConfig(new JsonObject().put("path", "application.yaml")))
                .addStore(new ConfigStoreOptions()
                        .setType("env"));
        return ConfigRetriever.create(vertx, options).getConfig();
    }
}
