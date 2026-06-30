package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.ConfigurationContext;
import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

import java.util.function.Function;

public interface GameManagerFactoryFactory extends OrbitalComponentFactory<GameManagerFactory> {
    static GameManagerFactoryFactory gameManagerFactory(DeploymentOptions options, Function<ConfigurationContext, GameManagerFactory> factoryConstructor) {
        return new GameManagerFactoryFactory() {
            @Override
            public Future<GameManagerFactory> create(ConfigurationContext config) {
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
