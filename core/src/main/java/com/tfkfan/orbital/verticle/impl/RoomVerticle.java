package com.tfkfan.orbital.verticle.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.verticle.BaseVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class RoomVerticle extends BaseVerticle {
    final Function<String, GameManager> gameManagerFactory;
    GameManager gameManager;

    public RoomVerticle(Function<String, GameManager> gameManagerFactory) {
        this.gameManagerFactory = gameManagerFactory;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);

        gameManager = gameManagerFactory.apply(verticleId);
        log.info("Room verticle with id {} started", verticleId);

        vertx.eventBus().send(Constants.GATEWAY_ROOM_CREATE_CHANNEL, null, new DeliveryOptions().addHeader(
                Constants.ROOM_VERTICAL_ID, verticleId
        ).setLocalOnly(true));
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        vertx.eventBus().send(Constants.GATEWAY_ROOM_DESTROY_CHANNEL, null, new DeliveryOptions().addHeader(
                Constants.ROOM_VERTICAL_ID, verticleId
        ).setLocalOnly(true));
        super.stop(stopPromise);
    }
}
