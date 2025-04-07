package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import lombok.Data;

@Data
public class Player2DInitPack implements InitPack {
    private final Long id;
    private final Vector2D position;
}
