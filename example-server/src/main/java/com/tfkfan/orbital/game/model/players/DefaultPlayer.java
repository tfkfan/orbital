package com.tfkfan.orbital.game.model.players;

import com.tfkfan.orbital.configuration.Constants;
import com.tfkfan.orbital.game.room.DefaultGameRoom;
import com.tfkfan.orbital.network.pack.InitPack;
import com.tfkfan.orbital.network.pack.PrivateUpdatePack;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.network.pack.init.PlayerInitPack;
import com.tfkfan.orbital.network.pack.update.PlayerUpdatePack;
import com.tfkfan.orbital.network.pack.update.PrivatePlayerUpdatePack;
import com.tfkfan.orbital.session.UserSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultPlayer extends BasePlayer2D<DefaultGameRoom> {
    protected Map<Direction, Boolean> movingState;

    public DefaultPlayer(Long id, DefaultGameRoom gameRoom, UserSession userSession) {
        super(id, gameRoom, userSession);
        movingState = Arrays.stream(Direction.values())
                .collect(Collectors.toMap(direction -> direction, _ -> false));
    }

    public void updateState(Direction direction, boolean state) {
        movingState.put(direction, state);
    }

    @Override
    public void update() {
        velocity.setX(movingState.get(Direction.RIGHT) ?
                Constants.ABS_PLAYER_SPEED : ( movingState.get(Direction.LEFT) ?
                -Constants.ABS_PLAYER_SPEED : 0.0));
        velocity.setY(movingState.get(Direction.UP) ?
                -Constants.ABS_PLAYER_SPEED : ( movingState.get(Direction.DOWN) ?
                Constants.ABS_PLAYER_SPEED : 0.0));

        super.update();
    }

    @Override
    public UpdatePack getUpdatePack() {
        return new PlayerUpdatePack(id, position);
    }

    @Override
    public InitPack getInitPack() {
        return new PlayerInitPack(id, position);
    }

    @Override
    public InitPack init() {
        return getInitPack();
    }

    @Override
    public PrivateUpdatePack getPrivateUpdatePack() {
        return new PrivatePlayerUpdatePack(id);
    }
}
