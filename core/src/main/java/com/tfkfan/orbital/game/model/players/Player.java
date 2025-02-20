package com.tfkfan.orbital.game.model.players;

import com.tfkfan.orbital.game.model.GameEntity;
import com.tfkfan.orbital.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.orbital.session.UserSession;

public interface Player extends GameEntity<Long>, IPrivateUpdatePackProvider {
    UserSession getUserSession();
}
