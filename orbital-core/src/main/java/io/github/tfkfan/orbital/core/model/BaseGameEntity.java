package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.room.GameRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class BaseGameEntity<ID, V extends Vector<V>> implements GameEntity<ID>, DynamicEntity<ID, V> {
    protected final GameRoom gameRoom;
    protected boolean isMoving = false;
    protected boolean isAlive = true;

    protected V position;
    protected V velocity;
    protected V acceleration;

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
