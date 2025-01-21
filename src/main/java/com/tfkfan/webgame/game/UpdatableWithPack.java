package com.tfkfan.webgame.game;

import com.tfkfan.webgame.network.pack.UpdatePack;

public interface UpdatableWithPack<T extends UpdatePack> {
    T update();
}
