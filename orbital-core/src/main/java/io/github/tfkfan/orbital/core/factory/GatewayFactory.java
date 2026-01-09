package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.OrbitalClusterManager;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.function.Function;
import java.util.function.Supplier;

public interface GatewayFactory extends OrbitalComponentFactory<GatewayVerticle> {
    static GatewayFactory websocket(Supplier<OrbitalClusterManager> clusterManagerSupplier) {
        return websocket(new DeploymentOptions(), clusterManagerSupplier, GatewayFactory.defaultCustomizer());
    }

    static GatewayFactory websocket(DeploymentOptions options,
                                    Supplier<OrbitalClusterManager> clusterManagerSupplier,
                                    Function<WebsocketGatewayFactory, GatewayFactory> customizer) {
        return config -> customizer.apply(new WebsocketGatewayFactory(clusterManagerSupplier.get(), options))
                .create(config);
    }

    static Function<WebsocketGatewayFactory, GatewayFactory> defaultCustomizer() {
        return it -> it.withStaticHandler("static")
                .withMonitoring(new MonitorEndpointFactory(CorsHandler.create("*")));
    }
}
