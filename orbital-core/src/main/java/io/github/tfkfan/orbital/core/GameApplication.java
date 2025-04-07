package io.github.tfkfan.orbital.core;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.RoomVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.stream.IntStream;

import static io.github.tfkfan.orbital.core.verticle.GameVerticle.run;

@Slf4j
public final class GameApplication {
    public static Future<?> runGame(Vertx vertx, GatewayVerticle gatewayVerticle, Function<String, GameManager> gameManagerFactory,
                                    DeploymentOptions gatewayOptions, DeploymentOptions roomOptions, RoomConfig roomConfig) {
        return run(vertx, gatewayVerticle, gatewayOptions)
                .flatMap(t -> runRooms(vertx, roomOptions, roomConfig, gameManagerFactory))
                .onSuccess(t -> log.info("Orbital has started successfully"));
    }

    public static CompositeFuture runRooms(Vertx vertx, DeploymentOptions options, RoomConfig roomConfig,
                                           Function<String, GameManager> gameManagerFactory) {
        return Future.all(IntStream.range(0, roomConfig.getInstances())
                .mapToObj(t -> vertx.deployVerticle(
                        new RoomVerticle(gameManagerFactory),
                        options)).toList());
    }
}
