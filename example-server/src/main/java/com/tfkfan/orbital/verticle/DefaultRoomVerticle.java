package com.tfkfan.orbital.verticle;

import com.tfkfan.orbital.RoomVerticle;
import com.tfkfan.orbital.manager.DefaultGameManager;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.properties.RoomProperties;
import io.vertx.core.Vertx;

public class DefaultRoomVerticle extends RoomVerticle {
    @Override
    protected GameManager<?, ?> createGameManager(String verticleId, Vertx vertx, RoomProperties properties) {
        return new DefaultGameManager(verticleId, vertx, properties);
    }
}
