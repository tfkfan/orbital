package io.github.tfkfan.orbital.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.manager.DefaultGameManager;
import io.github.tfkfan.orbital.core.monitor.MonitorEndpoint;
import io.github.tfkfan.orbital.core.monitor.MonitorableVertx;
import io.github.tfkfan.resources.GeometryResources;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        final Future<Vertx> vertx = new MonitorableVertx().build();

        Orbital.newCluster(new OrbitalBuilderImpl(vertx)
                        .withLocalConfig()
                        .withWebsocketGateway(it ->
                                it.withRouterInitializer(router -> router.route().handler(StaticHandler.create("static")))
                                        .withRouterInitializer(new MonitorEndpoint(CorsHandler.create("*"))::create))
                        .withGameManagerFactory(config -> DefaultGameManager.factory(new GeometryResources().load(), config.second().getRoom()))
                        .withRoomClusterLauncher(config -> new RoomDeploymentConfig(new DeploymentOptions()
                                .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                                .setWorkerPoolSize(100))))
                .onSuccess(orbital -> log.info("Orbital cluster is ready"));
    }
}
