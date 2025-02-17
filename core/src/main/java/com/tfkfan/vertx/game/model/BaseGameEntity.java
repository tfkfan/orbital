package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class BaseGameEntity<ID, GR extends GameRoom> implements GameEntity<ID> {
    protected final GR gameRoom;
    protected boolean isMoving = false;
    protected boolean isAlive = true;
    protected Vector2D position = new Vector2D();
    protected Vector2D velocity = new Vector2D();
    protected Vector2D acceleration = new Vector2D();

    @Override
    public void update() {
        isMoving = !velocity.isZero();
        velocity.sum(acceleration);
        position.sum(velocity);
    }
}
