package io.github.tfkfan.orbital.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.tfkfan.orbital.core.manager.DefaultGameRoomManager;
import io.github.tfkfan.orbital.core.monitor.MonitorableVertx;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.json.jackson.DatabindCodec;
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
                                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                                        .setWorkerPoolSize(100),
                                DefaultGameRoomManager.factory())
                )
                .onFailure(th -> log.error("Startup error", th))
                .onSuccess(orbital -> log.info("Orbital cluster is ready: {}", orbital.vertx().isClustered()));
    }
}
