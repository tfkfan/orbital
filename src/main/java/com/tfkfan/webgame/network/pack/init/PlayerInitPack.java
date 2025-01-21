package com.tfkfan.webgame.network.pack.init;

import com.tfkfan.webgame.math.Vector;
import com.tfkfan.webgame.network.pack.InitPack;
import lombok.Data;

@Data
public class PlayerInitPack implements InitPack {
    private final Long id;
    private final Vector position;
}
