package com.tfkfan.vertx.factory;

import com.tfkfan.vertx.game.map.GameMap;

import java.util.function.Supplier;

@FunctionalInterface
public interface GameMapFactory extends Supplier<GameMap> {
}
