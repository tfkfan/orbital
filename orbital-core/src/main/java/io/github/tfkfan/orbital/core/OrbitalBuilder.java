package io.github.tfkfan.orbital.core;


import io.vertx.core.Future;

public interface OrbitalBuilder {
    Future<Orbital> buildAndRun();
}
