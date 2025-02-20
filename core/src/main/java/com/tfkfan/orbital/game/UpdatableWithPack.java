package com.tfkfan.orbital.game;

import com.tfkfan.orbital.network.pack.UpdatePack;

public interface UpdatableWithPack<T extends UpdatePack> {
    T update();
}
