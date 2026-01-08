package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.function.Function;

public interface GatewayFactory extends OrbitalComponentFactory<GatewayVerticle> {
    static GatewayFactory websocket() {
        return websocket(new DeploymentOptions(), GatewayFactory.defaultCustomizer());
    }

    static GatewayFactory websocket(DeploymentOptions options, Function<WebsocketGatewayFactory, GatewayFactory> customizer) {
        return config -> customizer.apply(new WebsocketGatewayFactory(options))
                .create(config);
    }

    static Function<WebsocketGatewayFactory, GatewayFactory> defaultCustomizer() {
        return it -> it.withStaticHandler("static")
                .withMonitoring(new MonitorEndpointFactory(CorsHandler.create("*")));
    }
}
