package io.github.tfkfan.orbital.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.manager.DefaultGameManager;
import io.github.tfkfan.orbital.core.factory.MonitorEndpointFactory;
import io.github.tfkfan.orbital.core.monitor.MonitorableVertx;
import io.github.tfkfan.resources.GeometryResources;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    static {
        DatabindCodec.mapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static void main(String[] args) {
        Orbital.newCluster(OrbitalBuilder.create(MonitorableVertx.create())
                        .withConfig(ctx -> ctx.withExtension("gold", 100))
                        .withWebsocketGateway(new DeploymentOptions())
                        .withGameManagerFactory(new DeploymentOptions()
                                        .setInstances(3)
                                        .setWorkerPoolName("workingpool")
                                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD),
                                DefaultGameManager.factory())
                )
                .onFailure(th -> log.error("Startup error", th))
                .onSuccess(orbital -> log.info("Orbital cluster is ready"));
    }
}
