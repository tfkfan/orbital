package com.tfkfan.webgame.game;

import com.tfkfan.webgame.network.pack.InitPack;

public interface Initializable<IP extends InitPack> {
    IP init();
}
