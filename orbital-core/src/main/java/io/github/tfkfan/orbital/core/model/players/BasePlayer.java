package io.github.tfkfan.orbital.core.model.players;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.model.BaseGameEntity;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer<V extends Vector<V>> extends BaseGameEntity<Long,V> implements Player {
    protected final Long id;
    protected final PlayerSession playerSession;

    protected BasePlayer(Long id, GameRoom gameRoom, PlayerSession playerSession) {
        super(gameRoom);
        this.id = id;
        this.playerSession = playerSession;
        playerSession.setPlayer(this);
    }
}
