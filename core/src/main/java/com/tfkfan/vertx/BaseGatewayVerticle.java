package com.tfkfan.vertx;


import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.manager.MatchmakerManager;
import com.tfkfan.vertx.properties.ApplicationProperties;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseGatewayVerticle extends BaseVerticle {
    private MatchmakerManager matchmakerManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        final Router router = setupRouter();
        initRouter(router);

        try {
            final JsonObject cnf = config();
            final ApplicationProperties properties = cnf.mapTo(ApplicationProperties.class);
            final int port = properties.getServer().getPort();

            matchmakerManager = createMatchmakerManager(properties);

            vertx.createHttpServer().requestHandler(router).webSocketHandler(matchmakerManager).listen(port, (done) -> {
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

    //protected abstract void createAndConfigureServer(ApplicationProperties properties, int port );

    private void initRoomVerticles(Long roomVerticleInstances, JsonObject ymlConfig, Promise<Void> startPromise) {
        for (int i = 0; i < roomVerticleInstances; i++) {
            final String roomVerticleId = nextVerticleId();
            vertx.deployVerticle(() -> createRoomVerticle()
                            .verticleId(roomVerticleId)
                            .stopListener(
                                    _ -> matchmakerManager.onVerticleDisconnected(roomVerticleId)), new DeploymentOptions()
                            .setConfig( ymlConfig),
                    ar -> {
                        if (ar.succeeded()) {
                            matchmakerManager.onVerticleConnected(roomVerticleId);
                            log.info("Room verticle {} connected", roomVerticleId);
                            return;
                        }
                        log.error("Room verticle {} not initialized", roomVerticleId);
                        startPromise.fail(ar.cause());
                    });
        }
    }

    protected abstract RoomVerticle createRoomVerticle();

    protected void initRouter(Router router) {
    }

    protected abstract MatchmakerManager createMatchmakerManager(ApplicationProperties applicationProperties);

    private Router setupRouter() {
        Router router = Router.router(vertx);
        router.get("/health").handler(rc -> rc.response().end("OK"));
        return router;
    }
}
