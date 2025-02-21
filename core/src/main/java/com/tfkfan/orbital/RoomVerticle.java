package com.tfkfan.orbital;

import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.properties.ApplicationProperties;
import com.tfkfan.orbital.properties.RoomProperties;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RoomVerticle extends BaseVerticle {
    private GameManager<?, ?> gameManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        log.info("Starting room verticle with id {}", verticleId);

        try {
            final ApplicationProperties properties = config().mapTo(ApplicationProperties.class);
            gameManager = createGameManager(verticleId, vertx, properties.getRoom());
            startPromise.complete();
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }

    protected GameManager<?, ?> getGameManager() {
        return gameManager;
    }

    protected abstract GameManager<?, ?> createGameManager(String verticleId, Vertx vertx, RoomProperties properties);
}
