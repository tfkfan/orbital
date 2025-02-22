package com.tfkfan.orbital;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.properties.ApplicationProperties;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class GatewayVerticle extends BaseVerticle {
    protected Consumer<Router> routerInitializer = _ -> {
    };

    final int port;
    final MatchmakerManager matchmakerManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        final Router router = setupRouter();

        try {
            initGatewayConsumers();

            vertx.createHttpServer()
                    .requestHandler(router)
                    .webSocketHandler(matchmakerManager)
                    .listen(port)
                    .onSuccess(_ -> startPromise.complete())
                    .onFailure(startPromise::fail);
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }

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
                matchmakerManager.onVerticleConnected(msg.headers().get(Constants.ROOM_VERTICAL_ID)));
        vertx.eventBus().localConsumer(Constants.GATEWAY_ROOM_DESTROY_CHANNEL, msg ->
                matchmakerManager.onVerticleDisconnected(msg.headers().get(Constants.ROOM_VERTICAL_ID)));
    }
}
