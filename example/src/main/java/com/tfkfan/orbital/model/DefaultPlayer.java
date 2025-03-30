package com.tfkfan.orbital.model;

import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.model.players.BasePlayer;
import com.tfkfan.orbital.network.pack.InitPack;
import com.tfkfan.orbital.network.pack.PrivateUpdatePack;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.network.pack.init.Player2DInitPack;
import com.tfkfan.orbital.network.pack.update.Player2DUpdatePack;
import com.tfkfan.orbital.network.pack.update.PrivatePlayerUpdatePack;
import com.tfkfan.orbital.room.GameRoom;
import com.tfkfan.orbital.session.PlayerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultPlayer extends BasePlayer<Vector2D> {
    protected Map<Direction, Boolean> movingState;
    Double ABS_PLAYER_SPEED = 5.0;

    public DefaultPlayer(Long id, GameRoom gameRoom, PlayerSession userSession) {
        super(id, gameRoom, userSession);
        movingState = Arrays.stream(Direction.values())
                .collect(Collectors.toMap(direction -> direction, _ -> false));
        position = new Vector2D();
        velocity = new Vector2D();
        acceleration = new Vector2D();
    }

    public void updateState(Direction direction, boolean state) {
        movingState.put(direction, state);
    }

    @Override
    public void update(long dt) {
        velocity.setX(movingState.get(Direction.RIGHT) ?
                ABS_PLAYER_SPEED : (movingState.get(Direction.LEFT) ?
                -ABS_PLAYER_SPEED : 0.0));
        velocity.setY(movingState.get(Direction.UP) ?
                -ABS_PLAYER_SPEED : (movingState.get(Direction.DOWN) ?
                ABS_PLAYER_SPEED : 0.0));

        velocity.mult(2.0,2.0);
        super.update(dt);
    }

    @Override
    public UpdatePack getUpdatePack() {
        return new Player2DUpdatePack(id, position);
    }

    @Override
    public InitPack getInitPack() {
        return new Player2DInitPack(id, position);
    }

    @Override
    public PrivateUpdatePack getPrivateUpdatePack() {
        return new PrivatePlayerUpdatePack(id);
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }
}
