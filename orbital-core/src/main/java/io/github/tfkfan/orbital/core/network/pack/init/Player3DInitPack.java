package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.math.Vector3D;
import io.github.tfkfan.orbital.core.network.pack.InitPack;

public class Player3DInitPack implements InitPack {
    private final Long id;
    private final Vector3D position;

    public Player3DInitPack(Long id, Vector3D position) {
        this.id = id;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public Vector3D getPosition() {
        return position;
    }
}
