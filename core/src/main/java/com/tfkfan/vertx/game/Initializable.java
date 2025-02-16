package com.tfkfan.vertx.game;

import com.tfkfan.vertx.network.pack.InitPack;

public interface Initializable<IP extends InitPack> {
    IP init();
}
