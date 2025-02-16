package com.tfkfan.vertx.game.model.players;

import com.tfkfan.vertx.game.Updatable;
import com.tfkfan.vertx.game.map.GameMap;
import com.tfkfan.vertx.game.model.BaseGameEntity;
import com.tfkfan.vertx.game.model.Direction;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.math.Vector;
import com.tfkfan.vertx.network.pack.InitPack;
import com.tfkfan.vertx.network.pack.PrivateUpdatePack;
import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.vertx.session.UserSession;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class BasePlayer<GR extends GameRoom, IP extends InitPack, UP extends UpdatePack, PUP extends PrivateUpdatePack>
        extends BaseGameEntity<Long, GR, IP, UP> implements Player, Updatable, IPrivateUpdatePackProvider<PUP> {
    protected final Long id;
    protected final long cooldown;
    protected final GR gameRoom;
    protected final GameMap gameMap;
    protected final UserSession userSession;

    protected Vector target;

    protected Map<Direction, Boolean> movingState;

    protected BasePlayer(Long id, long cooldown, GR gameRoom, GameMap gameMap, UserSession userSession) {
        super(gameRoom);
        this.id = id;
        this.cooldown = cooldown;
        this.gameRoom = gameRoom;
        this.gameMap = gameMap;
        this.userSession = userSession;
        userSession.setPlayer(this);
        movingState = Arrays.stream(Direction.values()).collect(Collectors.toMap(direction -> direction, e -> false));
    }

    public void update() {
    }
}
