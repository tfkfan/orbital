package com.tfkfan.orbital.game.model.players;

import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.model.BaseGame2DEntity;
import com.tfkfan.orbital.game.room.GameRoom;
import com.tfkfan.orbital.session.UserSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer2D<GR extends GameRoom> extends BaseGame2DEntity<Long, GR> implements Player {
    protected final Long id;
    protected final GR gameRoom;
    protected final GameMap gameMap;
    protected final UserSession userSession;

    protected BasePlayer2D(Long id, GR gameRoom, UserSession userSession) {
        super(gameRoom);
        this.id = id;
        this.gameRoom = gameRoom;
        this.gameMap = gameRoom.gameMap();
        this.userSession = userSession;
        userSession.setPlayer(this);
    }
}
