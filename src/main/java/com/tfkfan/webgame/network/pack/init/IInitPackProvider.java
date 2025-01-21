package com.tfkfan.webgame.network.pack.init;

import com.tfkfan.webgame.network.pack.InitPack;

public interface IInitPackProvider<T extends InitPack> {
    T getInitPack();
}
