package io.github.tfkfan.orbital.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import io.github.tfkfan.orbital.core.manager.DefaultGameManager;
import io.github.tfkfan.orbital.core.monitor.MonitorEndpoint;
import io.github.tfkfan.orbital.core.monitor.MonitorableVertx;
import io.github.tfkfan.orbital.core.properties.ApplicationProperties;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.WebsocketGatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

import static io.github.tfkfan.orbital.core.GameApplication.runGame;
import static io.github.tfkfan.orbital.core.verticle.GameVerticle.loadConfig;
import static io.github.tfkfan.orbital.core.verticle.GameVerticle.startupErrorHandler;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Vertx vertx = new MonitorableVertx().build();

        loadConfig(vertx)
                .flatMap(cnf -> {
                    final ApplicationProperties properties = cnf.mapTo(ApplicationProperties.class);
                    final RoomConfig roomConfig = properties.getRoom();
                    final ServerConfig serverConfig = properties.getServer();
                    final GatewayVerticle gatewayVerticle = new WebsocketGatewayVerticle(serverConfig, roomConfig)
                            .withRouterInitializer(router -> router.route().handler(StaticHandler.create("static")))
                            .withRouterInitializer(MonitorEndpoint::create);

                    final DeploymentOptions gatewayOptions = new DeploymentOptions().setConfig(cnf);
                    final DeploymentOptions roomOptions = new DeploymentOptions(gatewayOptions)
                            .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                            .setWorkerPoolSize(100);

                    return runGame(vertx, gatewayVerticle, DefaultGameManager.gameManagerFactory(roomConfig),
                            gatewayOptions, roomOptions, roomConfig);
                })
                .onFailure(throwable -> startupErrorHandler(vertx, throwable));
    }
}
