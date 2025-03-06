package com.tfkfan.webgame.game.model.players;

import com.tfkfan.webgame.config.Constants;
import com.tfkfan.webgame.game.Updatable;
import com.tfkfan.webgame.game.map.GameMap;
import com.tfkfan.webgame.game.model.BaseGameEntity;
import com.tfkfan.webgame.game.model.Direction;
import com.tfkfan.webgame.game.room.DefaultGameRoom;
import com.tfkfan.webgame.network.pack.init.PlayerInitPack;
import com.tfkfan.webgame.network.pack.update.IPrivateUpdatePackProvider;
import com.tfkfan.webgame.network.pack.update.PlayerUpdatePack;
import com.tfkfan.webgame.network.pack.update.PrivatePlayerUpdatePack;
import com.tfkfan.webgame.session.UserSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class DefaultPlayer
        extends BaseGameEntity<Long, DefaultGameRoom, PlayerInitPack, PlayerUpdatePack> implements Player, Updatable, IPrivateUpdatePackProvider<PrivatePlayerUpdatePack> {
    protected final Long id;

    protected final DefaultGameRoom gameRoom;
    protected final GameMap gameMap;
    protected final UserSession userSession;
    protected Map<Direction, Boolean> movingState;

    public DefaultPlayer(Long id, DefaultGameRoom gameRoom, UserSession userSession) {
        super(gameRoom);
        this.id = id;
        this.gameRoom = gameRoom;
        this.gameMap = gameRoom.gameMap();
        this.userSession = userSession;
        userSession.setPlayer(this);
        movingState = Arrays.stream(Direction.values()).collect(Collectors.toMap(direction -> direction, e -> false));
    }

    public void updateState(Direction direction, boolean state) {
        movingState.put(direction, state);
        isMoving = this.movingState.containsValue(true);
    }

    @Override
    public void update() {
        velocity.setX(isMoving && movingState.get(Direction.RIGHT) ?
                Constants.ABS_PLAYER_SPEED : (isMoving && movingState.get(Direction.LEFT) ?
                -Constants.ABS_PLAYER_SPEED : 0.0));
        velocity.setY(isMoving && movingState.get(Direction.UP) ?
                -Constants.ABS_PLAYER_SPEED : (isMoving && movingState.get(Direction.DOWN) ?
                Constants.ABS_PLAYER_SPEED : 0.0));

        position.sum(velocity);
    }

    @Override
    public PlayerUpdatePack getUpdatePack() {
        return new PlayerUpdatePack(id, position);
    }

    @Override
    public PlayerInitPack getInitPack() {
        return new PlayerInitPack(id, position);
    }

    @Override
    public PlayerInitPack init() {
        return getInitPack();
    }

    @Override
    public PrivatePlayerUpdatePack getPrivateUpdatePack() {
        return new PrivatePlayerUpdatePack(id);
    }
}
