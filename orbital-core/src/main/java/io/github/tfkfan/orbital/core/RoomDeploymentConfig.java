package io.github.tfkfan.orbital.core;

import io.vertx.core.DeploymentOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public final class RoomDeploymentConfig {
    private final DeploymentOptions deploymentOptions;

    public RoomDeploymentConfig(DeploymentOptions deploymentOptions) {
        this.deploymentOptions = deploymentOptions;
    }
}
