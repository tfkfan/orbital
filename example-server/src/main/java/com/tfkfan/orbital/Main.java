package com.tfkfan.orbital;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.orbital.manager.DefaultGameManager;
import com.tfkfan.orbital.manager.DefaultMatchmakerManager;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.properties.ApplicationProperties;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        BaseVerticle.loadConfig(vertx)
                .onSuccess(cnf -> {
                    final ApplicationProperties properties = cnf.mapTo(ApplicationProperties.class);
                    final MatchmakerManager matchmakerManager = new DefaultMatchmakerManager(properties.getRoom());
                    vertx.deployVerticle(() -> new WebsocketGatewayVerticle(properties.getServer().getPort(), matchmakerManager)
                                            .withRouterInitializer(router -> router
                                                    .route()
                                                    .handler(StaticHandler.create("static"))),
                                    new DeploymentOptions().setConfig(cnf))
                            .compose(_ -> Future.all(
                                    IntStream.range(0, properties.getRoom().getRoomVerticleInstances())
                                            .mapToObj(_ -> vertx.deployVerticle(
                                                    new RoomVerticle((verticleId) -> new DefaultGameManager(verticleId, properties.getRoom())),
                                                    new DeploymentOptions()
                                                            .setConfig(cnf)
                                                            .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                                                            .setWorkerPoolSize(100))).toList()))
                            .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
                })
                .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
    }
}
