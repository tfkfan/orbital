package io.github.tfkfan.orbital.core.model.players;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.Direction;
import io.github.tfkfan.orbital.core.model.MotionType;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.network.pack.init.Player2DInitPack;
import io.github.tfkfan.orbital.core.network.pack.update.BasePrivatePlayerUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.update.Player2DUpdatePack;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class Player2D extends BasePlayer<Vector2D> {
    private final MotionType motionType;
    protected Vector2D absoluteVel;

    protected Map<Direction, Boolean> movingState;

    public Player2D(Long id,
                    GameRoom gameRoom,
                    PlayerSession userSession,
                    MotionType motionType,
                    Vector2D position,
                    Vector2D velocity,
                    Vector2D acceleration) {
        super(id, gameRoom, userSession);
        this.motionType = motionType;
        this.movingState = Arrays.stream(Direction.values())
                .collect(Collectors.toMap(direction -> direction, t -> false));
        this.position = Objects.isNull(position) ? new Vector2D() : position;
        this.velocity = Objects.isNull(position) ? new Vector2D() : velocity;
        this.acceleration = Objects.isNull(position) ? new Vector2D() : acceleration;

    }

    public Player2D(Long id,
                    GameRoom gameRoom,
                    PlayerSession userSession,
                    MotionType motionType,
                    Vector2D position,
                    Vector2D velocity) {
        this(id, gameRoom, userSession, motionType,
                position, velocity, null);
    }

    public Player2D(Long id,
                    GameRoom gameRoom,
                    PlayerSession userSession,
                    MotionType motionType) {
        this(id, gameRoom, userSession, motionType,
                null, null, null);
    }

    @Override
    public void init() {
        super.init();
        this.absoluteVel = Vector2D.abs(velocity);
    }

    public void updateState(Direction direction, boolean state) {
        movingState.put(direction, state);
    }

    @Override
    public void update(long dt) {
        if (MotionType.STATE.equals(motionType)) {
            velocity.setX(movingState.get(Direction.RIGHT) ?
                    absoluteVel.getX() : (movingState.get(Direction.LEFT) ?
                    -absoluteVel.getX() : 0.0));
            velocity.setY(movingState.get(Direction.UP) ?
                    -absoluteVel.getY() : (movingState.get(Direction.DOWN) ?
                    absoluteVel.getY() : 0.0));
        }

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
}
