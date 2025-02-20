package com.tfkfan.orbital.factory;

import com.tfkfan.orbital.game.map.GameMap;

import java.util.function.Supplier;

@FunctionalInterface
public interface GameMapFactory extends Supplier<GameMap> {
}
