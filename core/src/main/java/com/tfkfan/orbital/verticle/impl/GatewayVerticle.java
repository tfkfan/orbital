package com.tfkfan.orbital.verticle.impl;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.configuration.props.ServerConfig;
import com.tfkfan.orbital.manager.GatewayManager;
import com.tfkfan.orbital.verticle.BaseVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class GatewayVerticle extends BaseVerticle {
    protected Consumer<Router> routerInitializer = _ -> {
    };

    protected final ServerConfig serverConfig;
    protected final GatewayManager gatewayManager;
    protected HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        final Router router = setupRouter();

        try {
            initGatewayConsumers();

            server = vertx.createHttpServer()
                    .requestHandler(router);

            internalCustomize(server)
                    .flatMap(s -> s.listen(serverConfig.getPort()))
                    .onSuccess(_ -> {
                        log.info("Websocket server started on port {}", serverConfig.getPort());
                        startPromise.complete();
                    })
                    .onFailure(startPromise::fail);
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }

    Future<HttpServer> internalCustomize(HttpServer server) {
        try {
            customize(server);
            return Future.succeededFuture(server);
        } catch (Throwable e) {
            log.error("Http server customization error", e);
            return Future.failedFuture(e);
        }
    }

    abstract void customize(HttpServer server);

    public GatewayVerticle withRouterInitializer(Consumer<Router> routerInitializer) {
        this.routerInitializer = routerInitializer;
        return this;
    }

    private Router setupRouter() {
        Router router = Router.router(vertx);
        router.get("/health").handler(rc -> rc.response().end("OK"));

        if (routerInitializer != null)
            routerInitializer.accept(router);
        return router;
    }

    private void initGatewayConsumers() {
        vertx.eventBus().localConsumer(Constants.GATEWAY_ROOM_CREATE_CHANNEL, msg ->
                gatewayManager.getVerticleListener().onConnect(msg.headers().get(Constants.ROOM_VERTICAL_ID)));
        vertx.eventBus().localConsumer(Constants.GATEWAY_ROOM_DESTROY_CHANNEL, msg ->
                gatewayManager.getVerticleListener().onDisconnect(msg.headers().get(Constants.ROOM_VERTICAL_ID)));
    }
}
