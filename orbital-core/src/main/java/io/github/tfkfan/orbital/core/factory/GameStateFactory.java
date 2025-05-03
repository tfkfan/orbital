package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.state.GameState;

import java.util.function.Supplier;

@FunctionalInterface
public interface GameStateFactory<S extends GameState> extends Supplier<S> {
}
