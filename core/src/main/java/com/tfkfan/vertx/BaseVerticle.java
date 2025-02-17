package com.tfkfan.vertx;

import com.tfkfan.vertx.manager.StopListener;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

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
}
