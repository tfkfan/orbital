package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.math.Vector2D;

public class Player2DUpdatePack implements UpdatePack{
    private long id;
    private Vector2D position;

    public Player2DUpdatePack() {
    }

    public Player2DUpdatePack(long id, Vector2D position) {
        this.id = id;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public Player2DUpdatePack setId(long id) {
        this.id = id;
        return this;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Player2DUpdatePack setPosition(Vector2D position) {
        this.position = position;
        return this;
    }
}
