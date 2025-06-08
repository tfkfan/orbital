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

    private final Vertx vertx;
    private final ClusterManager clusterManager;
    private AsyncMap<String, GatewayInfo> gatewayInfoAsyncMap;

    private final Set<GatewayInfo> registrationBuff = new HashSet<>();

    public OrbitalClusterManagerImpl(Vertx vertx) {
        this.vertx = Objects.requireNonNull(vertx);
        this.clusterManager = Objects.requireNonNull(((VertxImpl) vertx).getClusterManager());
    }

    @Override
    public void registerGateway(int port) {
        registrationBuff.add(new GatewayInfo(clusterManager.getNodeId(), clusterManager.getNodeInfo().host(), port));
    }

    @Override
    public void postInitialization() {
        if (gatewayInfoAsyncMap == null) {
            vertx.sharedData().<String, GatewayInfo>getAsyncMap(ORBITAL_HTTP_SERVER_INFO_BUCKET)
                    .onSuccess(map -> {
                        gatewayInfoAsyncMap = map;
                        register(map);
                    })
                    .onFailure(t -> {
                        throw new RuntimeException(t);
                    });
            return;
        }

        register(gatewayInfoAsyncMap);
    }

    private void register(AsyncMap<String, GatewayInfo> map) {
        for (GatewayInfo gatewayInfo : registrationBuff)
            map.put(gatewayInfo.nodeId(), gatewayInfo)
                    .onSuccess((v) -> log.info("Orbital node gateway {} is registered", gatewayInfo.nodeId()));
        registrationBuff.clear();
    }

    @Override
    public Future<List<GatewayInfo>> getGatewayInfoList() {
        if (gatewayInfoAsyncMap == null)
            return Future.succeededFuture(Collections.emptyList());
        return gatewayInfoAsyncMap.values();
    }
}
