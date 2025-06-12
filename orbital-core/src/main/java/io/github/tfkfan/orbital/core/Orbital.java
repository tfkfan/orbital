package io.github.tfkfan.orbital.core;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.Objects;

public final class Orbital {
    private volatile static Orbital instance = null;

    private final Vertx vertx;
    private final OrbitalClusterManagerImpl orbitalManager;

    public static Orbital get() {
        return Objects.requireNonNull(instance, "");
    }

    public static Future<Orbital> newCluster(OrbitalBuilder builder) {
        if (instance != null)
            return Future.failedFuture(new IllegalStateException("Orbital already started"));

        return builder.buildAndRun().map(orbital -> {
            if (instance == null)
                instance = orbital;
            orbital.getOrbitalManager().postInitialization();
            return orbital;
        });
    }

    Orbital(Vertx vertx) {
        this(vertx, new OrbitalClusterManagerImpl(vertx));
    }

    Orbital(Vertx vertx, OrbitalClusterManagerImpl orbitalManager) {
        this.vertx = Objects.requireNonNull(vertx);
        this.orbitalManager = Objects.requireNonNull(orbitalManager);
    }

    public Vertx getVertx() {
        return vertx;
    }

    public OrbitalClusterManagerImpl getOrbitalManager() {
        return orbitalManager;
    }
}
