package io.github.tfkfan.orbital.core;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.github.tfkfan.orbital.core.factory.*;
import io.github.tfkfan.orbital.core.verticle.GameVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static io.github.tfkfan.orbital.core.verticle.GameVerticle.startupErrorHandler;

public final class OrbitalBuilderImpl implements OrbitalBuilder {
    private final Future<Vertx> vertxFuture;

    Vertx vertx;
    OrbitalClusterManager orbitalManager;

    private ConfigurationContextFactory configurationContextFactory = ConfigurationContextFactory.localConfig();
    private GatewayFactory gatewayFactory = GatewayFactory.websocket(() -> orbitalManager);
    private GameManagerFactoryFactory gameManagerFactoryFactory;

    OrbitalBuilderImpl(Future<Vertx> vertxFuture) {
        this.vertxFuture = Objects.requireNonNull(vertxFuture);
    }

    public OrbitalBuilderImpl withConfig(String fullPath) {
        this.configurationContextFactory = ConfigurationContextFactory.localConfig(fullPath, null);
        return this;
    }

    public OrbitalBuilderImpl withConfig(String fullPath, Consumer<ConfigurationContext> contextCustomizer) {
        this.configurationContextFactory = ConfigurationContextFactory.localConfig(fullPath, contextCustomizer);
        return this;
    }

    public OrbitalBuilderImpl withConfig(Consumer<ConfigurationContext> contextCustomizer) {
        this.configurationContextFactory = ConfigurationContextFactory.localConfig(null, contextCustomizer);
        return this;
    }

    public OrbitalBuilderImpl withGateway(GatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
        return this;
    }

    public OrbitalBuilderImpl withWebsocketGateway(DeploymentOptions options) {
        return withWebsocketGateway(options, GatewayFactory.defaultCustomizer());
    }

    public OrbitalBuilderImpl withWebsocketGateway(DeploymentOptions options, Function<WebsocketGatewayFactory, GatewayFactory> customizer) {
        gatewayFactory = GatewayFactory.websocket(options, () -> orbitalManager, customizer);
        return this;
    }

    public OrbitalBuilderImpl withGameManagerFactory(DeploymentOptions options, Function<OrbitalConfig, GameManagerFactory> function) {
        gameManagerFactoryFactory = GameManagerFactoryFactory.gameManagerFactory(options, function);
        return this;
    }

    @Override
    public Future<Orbital> buildAndRun() {
        return vertxFuture
                .flatMap(this::initialize)
                .flatMap(this::loadConfig)
                .flatMap(this::deployGateway)
                .flatMap(this::deployRooms)
                .map(ctx -> new Orbital(vertx, orbitalManager))
                .onFailure(throwable -> startupErrorHandler(vertx, throwable));
    }

    private Future<Vertx> initialize(Vertx vertx) {
        this.vertx = vertx;
        this.orbitalManager = new OrbitalClusterManagerImpl(vertx);
        return Future.succeededFuture(vertx);
    }

    private Future<ConfigurationContext> loadConfig(Vertx vertx) {
        return Objects.requireNonNull(configurationContextFactory, "Configuration context factory is required")
                .load();
    }

    private Future<ConfigurationContext> deployGateway(ConfigurationContext context) {
        return Objects.requireNonNull(gatewayFactory, "Gateway factory is required")
                .create(context.getConfig())
                .flatMap(gatewayVerticle -> GameVerticle.deploy(vertx, gatewayVerticle, gatewayVerticle.options()))
                .map(it -> context);
    }

    private Future<ConfigurationContext> deployRooms(ConfigurationContext context) {
        return Future.succeededFuture(Objects.requireNonNull(gameManagerFactoryFactory, "Game manager factory is required"))
                .flatMap(gameManagerFactoryFactory ->
                        gameManagerFactoryFactory.create(context.getConfig())
                                .flatMap(gameManagerFactory ->
                                        GameVerticle.deploy(vertx, gameManagerFactory, gameManagerFactoryFactory.getDeploymentOptions()).map(it -> context))
                );
    }
}
