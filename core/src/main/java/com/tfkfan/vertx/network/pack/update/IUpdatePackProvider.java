package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;

public interface IUpdatePackProvider<T extends UpdatePack> {
    T getUpdatePack();
}
