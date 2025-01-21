package com.tfkfan.webgame;

import com.tfkfan.webgame.config.Constants;
import com.tfkfan.webgame.config.Fields;
import com.tfkfan.webgame.manager.GameManager;
import com.tfkfan.webgame.properties.ApplicationProperties;
import com.tfkfan.webgame.shared.VertxUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
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
