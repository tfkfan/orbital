package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;

public class BasePrivatePlayerUpdatePack implements PrivateUpdatePack {
    private long id;

    public BasePrivatePlayerUpdatePack() {
    }

    public BasePrivatePlayerUpdatePack(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public BasePrivatePlayerUpdatePack setId(long id) {
        this.id = id;
        return this;
    }
}
