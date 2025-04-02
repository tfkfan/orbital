package com.tfkfan.orbital.verticle.impl;

import com.tfkfan.orbital.configuration.props.ServerConfig;
import com.tfkfan.orbital.manager.GatewayManager;
import com.tfkfan.orbital.verticle.BaseVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public abstract class GatewayVerticle extends BaseVerticle {
    protected final Collection<Consumer<Router>> routerInitializers = new ArrayList<>();
    protected final Collection<Consumer<HttpServer>> serverConsumers = new ArrayList<>();
    protected final ServerConfig serverConfig;
    protected final GatewayManager gatewayManager;

    protected HttpServer server;

    protected GatewayVerticle(ServerConfig serverConfig, GatewayManager gatewayManager) {
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
                    .onSuccess(_ -> {
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
