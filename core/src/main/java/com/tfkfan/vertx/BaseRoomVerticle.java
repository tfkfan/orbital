package com.tfkfan.vertx;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.players.Player;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.manager.GameManager;
import com.tfkfan.vertx.manager.StopListener;
import com.tfkfan.vertx.properties.ApplicationProperties;
import com.tfkfan.vertx.properties.RoomProperties;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseRoomVerticle extends BaseVerticle {
    private GameManager<?, ?> gameManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        log.info("Starting room verticle with id {}", verticleId);

        try {
            final ApplicationProperties properties = config().getJsonObject(Constants.LOCAL_CONFIG).mapTo(ApplicationProperties.class);
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
