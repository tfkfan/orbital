package io.github.tfkfan.orbital.core.verticle.impl;

import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import io.github.tfkfan.orbital.core.manager.GatewayManager;
import io.github.tfkfan.orbital.core.verticle.BaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class GatewayVerticle extends BaseVerticle {
    private final Logger log = LoggerFactory.getLogger(GatewayVerticle.class);

    protected final Collection<Consumer<Router>> routerInitializers = new ArrayList<>();
    protected final Collection<Consumer<HttpServer>> serverConsumers = new ArrayList<>();
    protected final ServerConfig serverConfig;
    protected final GatewayManager gatewayManager;

    protected HttpServer server;

    protected GatewayVerticle(ServerConfig serverConfig, GatewayManager gatewayManager){
        this(serverConfig, gatewayManager, null);
    }

    protected GatewayVerticle(ServerConfig serverConfig, GatewayManager gatewayManager, DeploymentOptions deploymentOptions) {
        super(deploymentOptions);

        this.serverConfig = serverConfig;
        this.gatewayManager = gatewayManager;

        withRouterInitializer(router -> router.get("/health").handler(rc -> rc.response().end(
                new JsonObject()
                        .put("orbital-server", new JsonObject()
                                .put("status", "UP")
                                .put("details", new JsonArray().add("GatewayVerticle").add("RoomVerticle")))
                        .encode())));
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        final Router router = setupRouter();

        try {
            server = vertx.createHttpServer()
                    .requestHandler(router);

            internalCustomize(server)
                    .flatMap(s -> s.listen(serverConfig.getPort()))
                    .onSuccess(t -> {
                        log.info("Websocket server started on port {}", serverConfig.getPort());
                        startPromise.complete();
                        routerInitializers.clear();
                        serverConsumers.clear();
                    })
                    .onFailure(startPromise::fail);
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }

    Future<HttpServer> internalCustomize(HttpServer server) {
        try {
            serverConsumers.forEach(it -> it.accept(server));
            return Future.succeededFuture(server);
        } catch (Throwable e) {
            log.error("Http server customization error", e);
            return Future.failedFuture(e);
        }
    }

    public GatewayVerticle withServerCustomizer(Consumer<HttpServer> serverConsumer) {
        this.serverConsumers.add(serverConsumer);
        return this;
    }

    public GatewayVerticle withRouterInitializer(Consumer<Router> routerInitializer) {
        this.routerInitializers.add(routerInitializer);
        return this;
    }

    private Router setupRouter() {
        final Router router = Router.router(vertx);
        routerInitializers.forEach(it -> it.accept(router));
        return router;
    }
}
