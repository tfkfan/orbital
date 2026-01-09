package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.OrbitalClusterManager;
import io.vertx.core.DeploymentOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class BaseGatewayFactory implements GatewayFactory {
    protected final Collection<Consumer<Router>> routerInitializers = new ArrayList<>();
    protected final OrbitalClusterManager clusterManager;
    protected DeploymentOptions options;

    public BaseGatewayFactory(OrbitalClusterManager clusterManager, DeploymentOptions options) {
        this.clusterManager = clusterManager;
        this.options = options;
    }

    public BaseGatewayFactory withRouter(Consumer<Router> initializer) {
        routerInitializers.add(initializer);
        return this;
    }

    public BaseGatewayFactory withStaticHandler(String path) {
        return withRouter(router -> router.route().handler(StaticHandler.create(path)));
    }

    public BaseGatewayFactory withCors(String pattern) {
        return withRouter(router -> router.route().handler(CorsHandler.create(pattern)));
    }

    public BaseGatewayFactory withMonitoring(MonitorEndpointFactory monitorEndpointFactory) {
        return withRouter(monitorEndpointFactory::create);
    }

    public BaseGatewayFactory withOptions(DeploymentOptions options) {
        this.options = options;
        return this;
    }
}