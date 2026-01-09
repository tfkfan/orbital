package io.github.tfkfan.orbital.core;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.spi.cluster.ClusterManager;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class OrbitalClusterManagerImpl implements OrbitalClusterManager {
    private static final String ORBITAL_HTTP_SERVER_INFO_BUCKET = "orbital.http-info";

    private final ClusterManager clusterManager;
    private AsyncMap<String, GatewayInfo> gatewayInfoAsyncMap;

    public OrbitalClusterManagerImpl(Vertx vertx) {
        Objects.requireNonNull(vertx);
        this.clusterManager = Objects.requireNonNull(((VertxImpl) vertx).getClusterManager());
        vertx.sharedData().<String, GatewayInfo>getAsyncMap(ORBITAL_HTTP_SERVER_INFO_BUCKET)
                .onSuccess(map -> gatewayInfoAsyncMap = map)
                .onFailure(t -> {
                    throw new RuntimeException(t);
                });
    }

    @Override
    public void registerGateway(int port) {
        var gatewayInfo = new GatewayInfo(clusterManager.getNodeId(), clusterManager.getNodeInfo().host(), port);
        gatewayInfoAsyncMap.put(gatewayInfo.nodeId(), gatewayInfo)
                .onSuccess((v) -> log.debug("Orbital node gateway {} is registered", gatewayInfo.nodeId()));
    }

    @Override
    public void unregisterGateway(int port) {
        var gatewayInfo = new GatewayInfo(clusterManager.getNodeId(), clusterManager.getNodeInfo().host(), port);

        gatewayInfoAsyncMap.remove(gatewayInfo.nodeId())
                .onSuccess((v) -> log.debug("Orbital node gateway {} is unregistered", gatewayInfo.nodeId()));
    }

    @Override
    public Future<List<GatewayInfo>> getGatewayInfoList() {
        if (gatewayInfoAsyncMap == null)
            return Future.succeededFuture(Collections.emptyList());
        return gatewayInfoAsyncMap.values();
    }
}
