package io.github.tfkfan.orbital.core.factory;

import io.vertx.core.DeploymentOptions;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseGameManagerFactory implements GameManagerFactory {
    private final DeploymentOptions deploymentOptions;
}
