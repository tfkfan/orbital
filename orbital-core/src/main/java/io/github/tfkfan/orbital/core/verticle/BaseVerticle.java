package io.github.tfkfan.orbital.core.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public abstract class BaseVerticle extends AbstractVerticle implements GameVerticle {
    protected String verticleId;

    public BaseVerticle() {
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        if (verticleId == null)
            verticleId = GameVerticle.nextVerticleId();
    }
}
