package com.tfkfan.vertx.game.model.players;

import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.BaseGameEntity;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.network.pack.InitPack;
import com.tfkfan.vertx.network.pack.PrivateUpdatePack;
import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.vertx.session.UserSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePlayer<GR extends GameRoom>
        extends BaseGameEntity<Long, GR>
        implements Player {
    protected final Long id;
    protected final GR gameRoom;
    protected final GameMap gameMap;
    protected final UserSession userSession;

    protected BasePlayer(Long id, GR gameRoom, UserSession userSession) {
        super(gameRoom);
        this.id = id;
        this.gameRoom = gameRoom;
        this.gameMap = gameRoom.gameMap();
        this.userSession = userSession;
        userSession.setPlayer(this);
    }

    public void update() {
    }
}
