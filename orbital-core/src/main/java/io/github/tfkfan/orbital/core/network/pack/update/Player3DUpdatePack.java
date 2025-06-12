package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.math.Vector3D;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;

public class Player3DUpdatePack implements UpdatePack{
    private long id;
    private Vector3D position;

    public Player3DUpdatePack() {
    }

    public Player3DUpdatePack(long id, Vector3D position) {
        this.id = id;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public Player3DUpdatePack setId(long id) {
        this.id = id;
        return this;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Player3DUpdatePack setPosition(Vector3D position) {
        this.position = position;
        return this;
    }
}
