package io.github.tfkfan.orbital.geo;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.players.BasePlayer;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.session.PlayerSession;

public class TestEntity extends BasePlayer<Vector2D> {
    public TestEntity(Long id, Vector2D position) {
        super(id, null, new PlayerSession());
        setPosition(position);
    }

    @Override
    public PrivateUpdatePack getPrivateUpdatePack() {
        return null;
    }

    @Override
    public InitPack getInitPack() {
        return null;
    }

    @Override
    public UpdatePack getUpdatePack() {
        return null;
    }
}