package com.tfkfan.vertx.factory;

import com.tfkfan.vertx.game.map.GameMap;

@FunctionalInterface
public interface GameMapFactory<GM extends GameMap> {
    GM createMap();
}
