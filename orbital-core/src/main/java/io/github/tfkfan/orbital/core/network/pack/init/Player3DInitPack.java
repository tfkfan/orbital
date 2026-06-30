package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import lombok.Data;

@Data
public class Player3DInitPack implements InitPack {
    private final Long id;
    private final Vector position;
}
