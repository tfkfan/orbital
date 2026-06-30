package io.github.tfkfan.orbital.core.verticle.impl;

import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.manager.GameRoomManager;
import io.github.tfkfan.orbital.core.verticle.BaseVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameRoomVerticle extends BaseVerticle {
    final GameRoomManager gameRoomManager;

    public GameRoomVerticle(GameManagerFactory gameManagerFactory) {
        super();
        this.gameRoomManager = gameManagerFactory.apply(verticleId());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        log.info("Room verticle with id {} started", verticleId());
    }
}
