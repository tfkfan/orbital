package com.tfkfan.vertx.verticle;

import com.tfkfan.vertx.RoomVerticle;
import com.tfkfan.vertx.manager.DefaultGameManager;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.properties.RoomProperties;
import io.vertx.core.Vertx;

public class DefaultRoomVerticle extends RoomVerticle {
    @Override
    protected GameManager<?, ?> createGameManager(String verticleId, Vertx vertx, RoomProperties properties) {
        return new DefaultGameManager(verticleId, vertx, properties);
    }
}
