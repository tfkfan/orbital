package io.github.tfkfan.orbital.core.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;

public abstract class BaseVerticle extends AbstractVerticle implements GameVerticle {
    private final String verticleId;
    private final DeploymentOptions options;

    public BaseVerticle() {
        this(null);
    }

    public BaseVerticle(DeploymentOptions options) {
        this.options = options != null ? options : new DeploymentOptions();
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

    public static DeliveryOptions defaults() {
        return new DeliveryOptions()
                .setLocalOnly(true)
                .setSendTimeout(5000);
    }
}
