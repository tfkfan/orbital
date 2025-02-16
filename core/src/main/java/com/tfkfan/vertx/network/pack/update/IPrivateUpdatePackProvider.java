package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.PrivateUpdatePack;

public interface IPrivateUpdatePackProvider<T extends PrivateUpdatePack> {
    T getPrivateUpdatePack();
}
