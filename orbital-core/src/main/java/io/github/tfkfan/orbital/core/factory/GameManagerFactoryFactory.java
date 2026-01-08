package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

import java.util.function.Function;

public interface GameManagerFactoryFactory extends OrbitalComponentFactory<GameManagerFactory> {
    static GameManagerFactoryFactory gameManagerFactory(DeploymentOptions options, Function<OrbitalConfig, GameManagerFactory> factoryConstructor) {
        return new GameManagerFactoryFactory() {
            @Override
            public Future<GameManagerFactory> create(OrbitalConfig config) {
                return Future.succeededFuture(factoryConstructor.apply(config));
            }

            @Override
            public DeploymentOptions getDeploymentOptions() {
                return options;
            }
        };
    }

    default DeploymentOptions getDeploymentOptions() {
        return new DeploymentOptions();
    }
}
