package com.tfkfan.webgame.network.pack.update;

import com.tfkfan.webgame.network.pack.UpdatePack;

public interface IUpdatePackProvider<T extends UpdatePack> {
    T getUpdatePack();
}
