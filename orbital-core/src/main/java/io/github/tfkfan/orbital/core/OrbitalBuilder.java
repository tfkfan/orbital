package io.github.tfkfan.orbital.core;


import io.vertx.core.Future;
import io.vertx.core.Vertx;

public interface OrbitalBuilder {
    Future<Orbital> buildAndRun();

    static OrbitalBuilderImpl create(Future<Vertx> vertxFuture) {
        return new OrbitalBuilderImpl(vertxFuture);
    }
}
