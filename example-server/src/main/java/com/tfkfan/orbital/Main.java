package com.tfkfan.orbital;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.orbital.manager.DefaultGameManager;
import com.tfkfan.orbital.manager.DefaultMatchmakerManager;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.manager.WebSocketManager;
import com.tfkfan.orbital.manager.impl.WebSocketManagerImpl;
import com.tfkfan.orbital.properties.ApplicationProperties;
import com.tfkfan.orbital.verticle.BaseVerticle;
import com.tfkfan.orbital.verticle.impl.RoomVerticle;
import com.tfkfan.orbital.verticle.impl.WebsocketGatewayVerticle;
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
                    final int port = properties.getServer().getPort();
                    final int roomVerticles = properties.getRoom().getRoomVerticleInstances();
                    final MatchmakerManager matchmakerManager = new DefaultMatchmakerManager(properties.getRoom().getMaxPlayers());
                    final WebSocketManager webSocketManager = new WebSocketManagerImpl(matchmakerManager);
                    final DeploymentOptions gatewayOptions = new DeploymentOptions().setConfig(cnf);
                    final DeploymentOptions roomOptions =  new DeploymentOptions(gatewayOptions)
                            .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                            .setWorkerPoolSize(100);

                    vertx.deployVerticle(() -> new WebsocketGatewayVerticle(port, webSocketManager)
                                            .withRouterInitializer(router -> router
                                                    .route()
                                                    .handler(StaticHandler.create("static"))),
                                    gatewayOptions)
                            .compose(_ -> {
                                log.info("Websocket server started on port {}", port);
                                return Future.all(IntStream.range(0, roomVerticles)
                                        .mapToObj(_ -> vertx.deployVerticle(
                                                new RoomVerticle((verticleId) -> DefaultGameManager.create(verticleId, properties.getRoom())),
                                                roomOptions)).toList());
                            })
                            .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
                })
                .onFailure(throwable -> BaseVerticle.startupErrorHandler(vertx, throwable));
    }
}
