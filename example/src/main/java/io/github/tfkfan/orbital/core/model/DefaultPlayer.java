package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.players.BasePlayer;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.network.pack.init.Player2DInitPack;
import io.github.tfkfan.orbital.core.network.pack.update.Player2DUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.update.BasePrivatePlayerUpdatePack;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultPlayer extends BasePlayer<Vector2D> {
    protected Map<Direction, Boolean> movingState;
    Double ABS_PLAYER_SPEED = 5.0;

    public DefaultPlayer(Long id,
                         GameRoom gameRoom,
                         PlayerSession userSession,
                         JsonObject inputData) {
        super(id, gameRoom, userSession);
        movingState = Arrays.stream(Direction.values())
                .collect(Collectors.toMap(direction -> direction, t -> false));
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

        velocity.mult(2.0, 2.0);
        super.update(dt);
    }

    @Override
    public UpdatePack getUpdatePack() {
        return new Player2DUpdatePack(getId(), position);
    }

    @Override
    public InitPack getInitPack() {
        return new Player2DInitPack(getId(), position);
    }

    @Override
    public PrivateUpdatePack getPrivateUpdatePack() {
        return new BasePrivatePlayerUpdatePack(getId());
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }
}
