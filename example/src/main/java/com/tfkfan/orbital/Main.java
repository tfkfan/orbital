package com.tfkfan.orbital;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.configuration.props.ServerConfig;
import com.tfkfan.orbital.manager.DefaultGameManager;
import com.tfkfan.orbital.monitor.MonitorEndpoint;
import com.tfkfan.orbital.monitor.MonitorableVertx;
import com.tfkfan.orbital.properties.ApplicationProperties;
import com.tfkfan.orbital.verticle.impl.GatewayVerticle;
import com.tfkfan.orbital.verticle.impl.WebsocketGatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

import static com.tfkfan.orbital.GameApplication.runGame;
import static com.tfkfan.orbital.verticle.GameVerticle.loadConfig;
import static com.tfkfan.orbital.verticle.GameVerticle.startupErrorHandler;

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
