package io.github.tfkfan.orbital.core.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

public abstract class BaseVerticle extends AbstractVerticle implements GameVerticle {
    protected String verticleId;
    private final DeploymentOptions options;

    public BaseVerticle() {
        this(null);
    }

    public BaseVerticle(DeploymentOptions options) {
        this.options = options != null ? options : new DeploymentOptions();
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        verticleId = GameVerticle.nextVerticleId();
    }

    @Override
    public String verticleId() {
        return verticleId;
    }

    @Override
    public DeploymentOptions options() {
        return options;
    }
}
