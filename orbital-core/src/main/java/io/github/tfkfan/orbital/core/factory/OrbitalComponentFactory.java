package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.vertx.core.Future;

@FunctionalInterface
public interface OrbitalComponentFactory<T> {
    Future<T> create(OrbitalConfig config);
}
