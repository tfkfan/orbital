package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.state.GameState;

import java.util.function.Supplier;

@FunctionalInterface
public interface GameStateFactory extends Supplier<GameState> {
}
