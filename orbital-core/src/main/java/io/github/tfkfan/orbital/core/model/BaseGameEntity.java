package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.room.GameRoom;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseGameEntity<ID extends Serializable, V extends Vector<V>> implements GameEntity<ID>, DynamicEntity<ID, V> {
    private final ID id;

    protected final GameRoom gameRoom;
    protected boolean isMoving = false;
    protected boolean isAlive = true;

    protected V position;
    protected V initialPosition;
    protected V velocity;
    protected V initialVelocity;
    protected V acceleration;
    protected V initialAcceleration;

    public BaseGameEntity(ID id, GameRoom gameRoom) {
        this.id = id;
        this.gameRoom = gameRoom;
    }

    public BaseGameEntity(ID id, GameRoom gameRoom, boolean isMoving, boolean isAlive, V position, V velocity, V acceleration) {
        this(id, gameRoom);
        this.isMoving = isMoving;
        this.isAlive = isAlive;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public <R extends GameRoom> R getGameRoom(Class<R> roomClass) {
        return roomClass.cast(gameRoom);
    }

    @Override
    public void update(long dt) {
        isMoving = velocity != null && !velocity.isZero();
        if (velocity != null) {
            if (acceleration != null)
                velocity.sum(acceleration);
            position.sum(velocity);
        }
    }

    public void init() {
        this.initialPosition = position != null ? position.clone() : null;
        this.initialVelocity = velocity != null ? velocity.clone() : null;
        this.initialAcceleration = acceleration != null ? acceleration.clone() : null;
    }
}
