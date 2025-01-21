package com.tfkfan.webgame.network.pack.update;

import com.tfkfan.webgame.network.pack.PrivateUpdatePack;

public interface IPrivateUpdatePackProvider<T extends PrivateUpdatePack> {
    T getPrivateUpdatePack();
}
