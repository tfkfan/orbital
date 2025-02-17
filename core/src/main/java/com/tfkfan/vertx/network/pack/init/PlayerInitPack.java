package com.tfkfan.vertx.network.pack.init;

import com.tfkfan.vertx.math.Vector2D;
import com.tfkfan.vertx.network.pack.InitPack;
import lombok.Data;

@Data
public class PlayerInitPack implements InitPack {
    private final Long id;
    private final Vector2D position;
}
