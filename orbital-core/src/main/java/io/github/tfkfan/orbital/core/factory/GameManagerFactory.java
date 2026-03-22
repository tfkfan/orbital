package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.manager.GameManager;
import io.vertx.core.Vertx;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface GameManagerFactory extends BiFunction<Vertx, String, GameManager> {
}
