package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.room.GameRoom;

import java.io.Serializable;

public abstract class BaseGameEntity<ID extends Serializable, V extends Vector<V>> implements GameEntity<ID>, DynamicEntity<ID, V> {
    private final ID id;

    protected final GameRoom gameRoom;
    protected boolean isMoving = false;
    protected boolean isAlive = true;

    protected V position;
    protected V velocity;
    protected V acceleration;

    public BaseGameEntity(ID id, GameRoom gameRoom) {
        this.id = id;
        this.gameRoom = gameRoom;
    }

    public BaseGameEntity(ID id, GameRoom gameRoom, boolean isMoving, boolean isAlive, V position, V velocity, V acceleration) {
        this.id = id;
        this.gameRoom = gameRoom;
        this.isMoving = isMoving;
        this.isAlive = isAlive;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    public V getPosition() {
        return position;
    }

    @Override
    public void setPosition(V position) {
        this.position = position;
    }

    @Override
    public V getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(V velocity) {
        this.velocity = velocity;
    }

    @Override
    public V getAcceleration() {
        return acceleration;
    }

    @Override
    public void setAcceleration(V acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public GameRoom getGameRoom() {
        return gameRoom;
    }

    @Override
    public boolean isMoving() {
        return isMoving;
    }

    public BaseGameEntity<ID, V> setMoving(boolean moving) {
        isMoving = moving;
        return this;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    public BaseGameEntity<ID, V> setAlive(boolean alive) {
        isAlive = alive;
        return this;
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
    }
}
