package com.tfkfan.orbital;

import com.tfkfan.orbital.network.pack.UpdatePack;

public interface UpdatableWithPack<T extends UpdatePack> {
    T update();
}
