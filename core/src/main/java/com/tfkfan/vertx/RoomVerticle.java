package com.tfkfan.vertx;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.properties.ApplicationProperties;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomVerticle extends AbstractVerticle {
    private String verticleId;
    private GameManager gameManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        verticleId = config().getString(Constants.ROOM_VERTICAL_ID);
        log.info("Starting room verticle with id {}", verticleId);

        try {
            final ApplicationProperties properties = config().getJsonObject(Constants.LOCAL_CONFIG).mapTo(ApplicationProperties.class);
            gameManager = new GameManager(verticleId, vertx, properties.getRoom());
            startPromise.complete();
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }
}
