package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.math.Vector2D;
import lombok.*;

@Getter
@Setter
public abstract class BaseGame2DEntity<ID, GR extends GameRoom> extends BaseGameEntity<ID, GR> {
    protected Vector2D position = new Vector2D();
    protected Vector2D velocity = new Vector2D();
    protected Vector2D acceleration = new Vector2D();

    public BaseGame2DEntity(GR gameRoom, boolean isMoving, boolean isAlive) {
        super(gameRoom, isMoving, isAlive);
    }

    public BaseGame2DEntity(GR gameRoom) {
        super(gameRoom);
    }

    @Override
    public void update() {
        isMoving = !velocity.isZero();
        velocity.sum(acceleration);
        position.sum(velocity);
    }
}
