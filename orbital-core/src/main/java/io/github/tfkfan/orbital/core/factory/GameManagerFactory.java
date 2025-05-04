package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.manager.GameManager;

import java.util.function.Function;

public interface GameManagerFactory extends Function<String, GameManager> {
}
