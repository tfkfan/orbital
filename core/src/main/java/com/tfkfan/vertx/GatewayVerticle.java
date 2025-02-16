package com.tfkfan.vertx;


import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.manager.MatchmakerManager;
import com.tfkfan.vertx.properties.ApplicationProperties;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class GatewayVerticle extends AbstractVerticle {
    private MatchmakerManager matchmakerManager;

    @Override
    public void start(Promise<Void> startPromise) {
        final Router router = setupRouter();
        router.route().handler(StaticHandler.create("static"));

        try {
            final JsonObject cnf = config().getJsonObject(Constants.LOCAL_CONFIG);
            final ApplicationProperties properties = cnf.mapTo(ApplicationProperties.class);
            final int httpPort = properties.getServer().getPort();

            matchmakerManager = new MatchmakerManager(properties.getRoom());

            vertx.createHttpServer().requestHandler(router).webSocketHandler(matchmakerManager).listen(httpPort, (done) -> {
                if (done.succeeded()) {
                    log.info("HTTP server started on port {}", done.result().actualPort());
                    initRoomVerticles(properties.getRoom().getRoomVerticleInstances(), cnf, startPromise);
                    return;
                }

                startPromise.fail(done.cause());
            });
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }

    private void initRoomVerticles(Long roomVerticleInstances, JsonObject ymlConfig, Promise<Void> startPromise) {
        for (int i = 0; i < roomVerticleInstances; i++) {
            final String roomVerticleId = UUID.randomUUID().toString();
            vertx.deployVerticle(() -> new RoomVerticle() {
                @Override
                public void stop(Promise<Void> stopPromise) {
                    matchmakerManager.onVerticleDisconnected(roomVerticleId);
                }
            }, new DeploymentOptions().setConfig(new JsonObject().put(Constants.LOCAL_CONFIG, ymlConfig).put(Constants.ROOM_VERTICAL_ID, roomVerticleId)), ar -> {
                if (ar.succeeded()) {
                    matchmakerManager.onVerticleConnected(roomVerticleId);
                    log.info("Room verticle {} connected", roomVerticleId);
                } else {
                    log.error("Room verticle {} not initialized", roomVerticleId);
                    startPromise.fail(ar.cause());
                }
            });
        }
    }

    private Router setupRouter() {
        Router router = Router.router(vertx);
        router.get("/health").handler(rc -> rc.response().end("OK"));
        return router;
    }
}
