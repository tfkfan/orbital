package com.tfkfan.orbital.network.pack.init;

import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.network.pack.InitPack;
import lombok.Data;

@Data
public class PlayerInitPack implements InitPack {
    private final Long id;
    private final Vector2D position;
}
