package io.github.tfkfan.orbital.core.verticle.impl;

import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.verticle.BaseVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameRoomVerticle extends BaseVerticle {
    GameManager gameManager;
    final GameManagerFactory gameManagerFactory;

    public GameRoomVerticle(GameManagerFactory gameManagerFactory) {
        super();
        this.gameManagerFactory = gameManagerFactory;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        this.gameManager = gameManagerFactory.apply(vertx, verticleId());
        log.info("Room verticle with id {} started", verticleId());
    }
}
