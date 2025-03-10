package com.tfkfan.orbital.model.players;

import com.tfkfan.orbital.math.Vector;
import com.tfkfan.orbital.model.BaseGameEntity;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.session.Session;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer<V extends Vector<V>> extends BaseGameEntity<Long,V> implements Player {
    protected final Long id;
    protected final PlayerSession userSession;

    protected BasePlayer(Long id, GameRoom gameRoom, PlayerSession userSession) {
        super(gameRoom);
        this.id = id;
        this.userSession = userSession;
        userSession.setPlayer(this);
    }
}
