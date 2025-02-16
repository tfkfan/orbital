package com.tfkfan.vertx.game;

import com.tfkfan.vertx.network.pack.UpdatePack;

public interface UpdatableWithPack<T extends UpdatePack> {
    T update();
}
