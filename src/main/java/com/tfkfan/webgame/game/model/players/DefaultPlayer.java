package com.tfkfan.webgame.game.model.players;

import com.tfkfan.webgame.config.Constants;
import com.tfkfan.webgame.game.model.Direction;
import com.tfkfan.webgame.game.room.DefaultGameRoom;
import com.tfkfan.webgame.network.pack.init.PlayerInitPack;
import com.tfkfan.webgame.network.pack.update.PlayerUpdatePack;
import com.tfkfan.webgame.network.pack.update.PrivatePlayerUpdatePack;
import com.tfkfan.webgame.session.UserSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultPlayer extends BasePlayer<DefaultGameRoom, PlayerInitPack, PlayerUpdatePack, PrivatePlayerUpdatePack> {
    public DefaultPlayer(Long id, DefaultGameRoom gameRoom, UserSession userSession) {
        super(id, Constants.DEFAULT_COOLDOWN, gameRoom, gameRoom.gameMap(), userSession);
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
        super.update();
    }

    @Override
    public PlayerUpdatePack getUpdatePack() {
        return new PlayerUpdatePack(
                id,
                position
        );
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
