package com.tfkfan.webgame.game.model.players;

import com.tfkfan.webgame.game.Updatable;
import com.tfkfan.webgame.game.map.GameMap;
import com.tfkfan.webgame.game.model.BaseGameEntity;
import com.tfkfan.webgame.game.model.Direction;
import com.tfkfan.webgame.game.room.GameRoom;
import com.tfkfan.webgame.math.Vector;
import com.tfkfan.webgame.network.pack.InitPack;
import com.tfkfan.webgame.network.pack.PrivateUpdatePack;
import com.tfkfan.webgame.network.pack.UpdatePack;
import com.tfkfan.webgame.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.webgame.session.UserSession;
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
