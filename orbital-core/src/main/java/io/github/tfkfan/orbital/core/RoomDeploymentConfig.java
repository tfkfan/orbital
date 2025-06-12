package io.github.tfkfan.orbital.core;

import io.vertx.core.DeploymentOptions;
public final class RoomDeploymentConfig {
    private final DeploymentOptions deploymentOptions;

    public RoomDeploymentConfig(DeploymentOptions deploymentOptions) {
        this.deploymentOptions = deploymentOptions;
    }

    public DeploymentOptions getDeploymentOptions() {
        return deploymentOptions;
    }
}
