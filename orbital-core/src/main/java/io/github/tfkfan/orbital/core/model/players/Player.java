package io.github.tfkfan.orbital.core.model.players;

import io.github.tfkfan.orbital.core.model.GameEntity;
import io.github.tfkfan.orbital.core.network.pack.IPrivateUpdatePackProvider;
import io.github.tfkfan.orbital.core.session.PlayerSession;

public interface Player extends GameEntity<Long>, IPrivateUpdatePackProvider {
    PlayerSession getPlayerSession();
    boolean isNpc();
}
