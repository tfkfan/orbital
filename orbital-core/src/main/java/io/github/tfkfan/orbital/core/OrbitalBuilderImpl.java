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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static io.github.tfkfan.orbital.core.verticle.GameVerticle.loadConfig;
import static io.github.tfkfan.orbital.core.verticle.GameVerticle.startupErrorHandler;

public final class OrbitalBuilderImpl implements OrbitalBuilder {
    private final static String DEFAULT_CONFIG_PATH = "application.yaml";
    private final static Integer DEFAULT_SERVER_PORT = 8080;

    private final Future<Vertx> vertxFuture;

    Vertx vertx;
    OrbitalClusterManagerImpl orbitalManager;

    private Supplier<Future<Pair<JsonObject, OrbitalConfig>>> configSupplier;
    private Function<OrbitalConfig, Future<GatewayVerticle>> gateway;
    private Function<Pair<JsonObject, OrbitalConfig>, Future<GameManagerFactory>> gameManagerFactory;
    private Function<OrbitalConfig, Future<RoomDeploymentConfig>> roomClusterConfig;

    public OrbitalBuilderImpl(Vertx vertx) {
        this.vertxFuture = Future.succeededFuture(vertx);
    }

    public OrbitalBuilderImpl(Future<Vertx> vertxFuture) {
        this.vertxFuture = Objects.requireNonNull(vertxFuture);
    }

    public OrbitalBuilderImpl withConfig(int roomVerticleInstances, RoomConfig roomConfig) {
        return withConfig(new OrbitalConfig(new ServerConfig(DEFAULT_SERVER_PORT, roomVerticleInstances), roomConfig));
    }

    public OrbitalBuilderImpl withConfig(OrbitalConfig orbitalConfig) {
        configSupplier = () -> loadLocalConfig().flatMap(it -> Future.succeededFuture(new Pair<>(it, orbitalConfig)));
        return this;
    }

    public <C extends OrbitalConfig> OrbitalBuilderImpl withConfig(String fullPath, Class<C> configClazz) {
        configSupplier = () -> loadLocalConfig(fullPath).map(cnf -> new Pair<>(cnf, cnf.mapTo(configClazz)));
        return this;
    }

    public OrbitalBuilderImpl withConfig(String fullPath) {
        return withConfig(fullPath, OrbitalConfig.class);
    }

    public OrbitalBuilderImpl withLocalConfig() {
        return withConfig(DEFAULT_CONFIG_PATH);
    }

    public OrbitalBuilderImpl withGateway(Function<OrbitalConfig, GatewayVerticle> function) {
        gateway = (configFuture) -> {
            orbitalManager.registerGateway(configFuture.getServer().getPort());
            return Future.succeededFuture(function.apply(configFuture));
        };
        return this;
    }

    public OrbitalBuilderImpl withWebsocketGateway(Consumer<WebsocketGatewayVerticle> customizer) {
        return withGateway(config -> {
            final var res = new WebsocketGatewayVerticle(config, new DeploymentOptions());
            customizer.accept(res);
            return res;
        });
    }

    public OrbitalBuilderImpl withGameManagerFactory(Function<Pair<JsonObject, OrbitalConfig>, GameManagerFactory> function) {
        gameManagerFactory = (config) -> Future.succeededFuture(function.apply(config));
        return this;
    }

    public OrbitalBuilderImpl withRoomClusterLauncher(Function<OrbitalConfig, RoomDeploymentConfig> function) {
        roomClusterConfig = (configFuture) -> Future.succeededFuture(function.apply(configFuture));
        return this;
    }

    @Override
    public Future<Orbital> buildAndRun() {
        return vertxFuture.map(v -> {
                    this.vertx = v;
                    this.orbitalManager = new OrbitalClusterManagerImpl(vertx);
                    return v;
                })
                .flatMap(vertx -> Objects.requireNonNull(configSupplier, "Config supplier is required").get())
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
                .map(o -> new Orbital(vertx, orbitalManager))
                .onFailure(throwable -> startupErrorHandler(vertx, throwable));
    }


    private Future<JsonObject> loadLocalConfig() {
        return loadLocalConfig(null);
    }

    private Future<JsonObject> loadLocalConfig(final String fullPath) {
        return loadConfig(vertx, fullPath);
    }

    private CompositeFuture runRooms(Vertx vertx,
                                     DeploymentOptions options,
                                     int instances,
                                     GameManagerFactory gameManagerFactory) {
        return Future.all(IntStream.range(0, instances)
                .mapToObj(t -> vertx.deployVerticle(new RoomVerticle(gameManagerFactory), options)).toList());
    }
}
