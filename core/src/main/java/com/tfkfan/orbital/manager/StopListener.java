package com.tfkfan.orbital.manager;

import io.vertx.core.Promise;

@FunctionalInterface
public interface StopListener {
    void stop(Promise<Void> stopPromise);
}
