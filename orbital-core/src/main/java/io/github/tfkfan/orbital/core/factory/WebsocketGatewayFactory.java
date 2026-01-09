package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.OrbitalClusterManager;
import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.WebsocketGatewayVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class WebsocketGatewayFactory extends BaseGatewayFactory {
    public WebsocketGatewayFactory(OrbitalClusterManager clusterManager, DeploymentOptions options) {
        super(clusterManager, options);
    }

    @Override
    public Future<GatewayVerticle> create(OrbitalConfig config) {
        final WebsocketGatewayVerticle v = new WebsocketGatewayVerticle(config, options, clusterManager);
        routerInitializers.forEach(v::withRouterInitializer);
        return Future.succeededFuture(v);
    }
}