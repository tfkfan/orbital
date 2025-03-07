package com.tfkfan.orbital;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.verticle.impl.GatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.function.Function;

import static com.tfkfan.orbital.verticle.GameVerticle.run;
import static com.tfkfan.orbital.verticle.impl.RoomVerticle.runRooms;

public class GameApplication {
    public static Future<?> runGame(Vertx vertx, GatewayVerticle gatewayVerticle, Function<String, GameManager> gameManagerFactory,
                             DeploymentOptions gatewayOptions, DeploymentOptions roomOptions, RoomConfig roomConfig) {
        return run(vertx, gatewayVerticle, gatewayOptions)
                .flatMap(_ -> runRooms(vertx, roomOptions, roomConfig, gameManagerFactory));
    }
}
