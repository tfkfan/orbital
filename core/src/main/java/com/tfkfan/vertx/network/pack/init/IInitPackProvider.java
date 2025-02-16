package com.tfkfan.vertx.network.pack.init;

import com.tfkfan.vertx.network.pack.InitPack;

public interface IInitPackProvider<T extends InitPack> {
    T getInitPack();
}
