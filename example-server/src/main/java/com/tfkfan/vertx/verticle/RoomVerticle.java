package com.tfkfan.vertx.verticle;

import com.tfkfan.vertx.BaseRoomVerticle;
import com.tfkfan.vertx.manager.DefaultGameManager;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.properties.RoomProperties;
import io.vertx.core.Vertx;

public class RoomVerticle extends BaseRoomVerticle {
    @Override
    protected GameManager<?, ?> createGameManager(String verticleId, Vertx vertx, RoomProperties properties) {
        return new DefaultGameManager(verticleId, vertx, properties);
    }
}
