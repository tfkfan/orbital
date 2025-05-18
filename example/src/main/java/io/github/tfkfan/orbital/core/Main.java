package io.github.tfkfan.orbital.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.manager.DefaultGameManager;
import io.github.tfkfan.orbital.core.monitor.MonitorEndpoint;
import io.github.tfkfan.orbital.core.monitor.MonitorableVertx;
import io.github.tfkfan.resources.GeometryResources;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Vertx vertx = new MonitorableVertx().build();

        new Orbital(vertx)
                .withLocalConfig()
                .withWebsocketGateway(it ->
                        it.withRouterInitializer(router -> router.route().handler(StaticHandler.create("static")))
                                .withRouterInitializer(MonitorEndpoint::create))
                .withGameManagerFactory(config -> DefaultGameManager.factory(new GeometryResources().load(), config.second().getRoom()))
                .withRoomClusterLauncher(config -> new RoomDeploymentConfig(new DeploymentOptions()
                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                        .setWorkerPoolSize(100)))
                .run();
    }
}
