package io.github.tfkfan.orbital.core;

import io.vertx.core.Future;

import java.util.List;

public interface OrbitalClusterManager {
    void registerGateway(int port);

    Future<List<GatewayInfo>> getGatewayInfoList();

    void postInitialization();
}
