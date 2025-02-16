package com.tfkfan.vertx.manager;

import io.vertx.core.Promise;

@FunctionalInterface
public interface StopListener {
    void stop(Promise<Void> stopPromise);
}
