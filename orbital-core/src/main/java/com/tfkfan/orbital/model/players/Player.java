package com.tfkfan.orbital.model.players;

import com.tfkfan.orbital.model.GameEntity;
import com.tfkfan.orbital.network.pack.IPrivateUpdatePackProvider;
import com.tfkfan.orbital.session.PlayerSession;

public interface Player extends GameEntity<Long>, IPrivateUpdatePackProvider {
    PlayerSession getPlayerSession();
}
