package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.network.pack.InitPack;

public class Player2DInitPack implements InitPack {
    private final Long id;
    private final Vector2D position;

    public Player2DInitPack(Long id, Vector2D position) {
        this.id = id;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public Vector2D getPosition() {
        return position;
    }
}
