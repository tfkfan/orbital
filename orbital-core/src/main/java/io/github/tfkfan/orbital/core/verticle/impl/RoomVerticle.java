package io.github.tfkfan.orbital.core.verticle.impl;

import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.verticle.BaseVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class RoomVerticle extends BaseVerticle {
    final GameManagerFactory gameManagerFactory;
    GameManager gameManager;

    public RoomVerticle(GameManagerFactory gameManagerFactory) {
        this.gameManagerFactory = gameManagerFactory;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);

        gameManager = gameManagerFactory.apply(verticleId);

        log.info("Room verticle with id {} started", verticleId);

        startPromise.complete();
    }
}
