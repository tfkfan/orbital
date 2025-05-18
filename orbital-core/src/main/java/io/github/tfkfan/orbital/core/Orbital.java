package io.github.tfkfan.orbital.core;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import io.github.tfkfan.orbital.core.factory.GameManagerFactory;
import io.github.tfkfan.orbital.core.shared.Pair;
import io.github.tfkfan.orbital.core.verticle.GameVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.GatewayVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.RoomVerticle;
import io.github.tfkfan.orbital.core.verticle.impl.WebsocketGatewayVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static io.github.tfkfan.orbital.core.verticle.GameVerticle.loadConfig;
import static io.github.tfkfan.orbital.core.verticle.GameVerticle.startupErrorHandler;

@Slf4j
public final class Orbital {
    private final static String DEFAULT_CONFIG_PATH = "application.yaml";
    private final static Integer DEFAULT_SERVER_PORT = 8080;

    private final Vertx vertx;

    private Supplier<Future<Pair<JsonObject, OrbitalConfig>>> configSupplier;
    private Function<OrbitalConfig, Future<GatewayVerticle>> gateway;
    private Function<Pair<JsonObject, OrbitalConfig>, Future<GameManagerFactory>> gameManagerFactory;
    private Function<OrbitalConfig, Future<RoomDeploymentConfig>> roomClusterConfig;

    public Orbital(Vertx vertx) {
        this.vertx = vertx;
    }

    public Orbital withConfig(int roomVerticleInstances, RoomConfig roomConfig) {
        return withConfig(new OrbitalConfig(new ServerConfig(DEFAULT_SERVER_PORT, roomVerticleInstances), roomConfig));
    }

    public Orbital withConfig(OrbitalConfig orbitalConfig) {
        configSupplier = () -> loadConfig(vertx).flatMap(it -> Future.succeededFuture(new Pair<>(it, orbitalConfig)));
        return this;
    }

    public Orbital withConfig(String fullPath) {
        configSupplier = () -> loadConfig(vertx, fullPath).map(cnf -> new Pair<>(cnf, cnf.mapTo(OrbitalConfig.class)));
        return this;
    }

    public Orbital withLocalConfig() {
        return withConfig(DEFAULT_CONFIG_PATH);
    }

    public Orbital withGateway(Function<OrbitalConfig, GatewayVerticle> function) {
        gateway = (configFuture) -> Future.succeededFuture(function.apply(configFuture));
        return this;
    }

    public Orbital withWebsocketGateway(Consumer<WebsocketGatewayVerticle> customizer) {
        return withGateway(config -> {
            final var res = new WebsocketGatewayVerticle(config, new DeploymentOptions());
            customizer.accept(res);
            return res;
        });
    }

    public Orbital withGameManagerFactory(Function<Pair<JsonObject, OrbitalConfig>, GameManagerFactory> function) {
        gameManagerFactory = (config) -> Future.succeededFuture(function.apply(config));
        return this;
    }

    public Orbital withRoomClusterLauncher(Function<OrbitalConfig, RoomDeploymentConfig> function) {
        roomClusterConfig = (configFuture) -> Future.succeededFuture(function.apply(configFuture));
        return this;
    }

    public void run() {
        Objects.requireNonNull(configSupplier, "Config supplier is required")
                .get()
                .flatMap(config -> Objects.requireNonNull(gateway, "Gateway is required").apply(config.second())
                        .flatMap(gatewayVerticle -> GameVerticle.run(vertx, gatewayVerticle, gatewayVerticle.options()))
                        .flatMap(deployment ->
                                Objects.requireNonNull(gameManagerFactory, "Game manager factory is required").apply(config)
                                        .flatMap(
                                                gameManagerFactory -> Objects.requireNonNull(roomClusterConfig, "Room cluster config is required").apply(config.second())
                                                        .flatMap(roomDeploymentConfig -> runRooms(vertx,
                                                                roomDeploymentConfig.getDeploymentOptions(),
                                                                config.second().getServer().getRoomVerticleInstances(),
                                                                gameManagerFactory
                                                        ))
                                        ))
                        .flatMap(gameManagerFactory -> Future.succeededFuture(config))
                )
                .onFailure(throwable -> startupErrorHandler(vertx, throwable));
    }

    private CompositeFuture runRooms(Vertx vertx,
                                     DeploymentOptions options,
                                     int instances,
                                     GameManagerFactory gameManagerFactory) {
        return Future.all(IntStream.range(0, instances)
                .mapToObj(t -> vertx.deployVerticle(new RoomVerticle(gameManagerFactory), options)).toList());
    }
}
