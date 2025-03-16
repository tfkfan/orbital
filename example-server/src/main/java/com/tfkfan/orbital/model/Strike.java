package com.tfkfan.orbital.model;

import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.network.pack.InitPack;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.room.GameRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Strike extends BaseGameEntity<Long, Vector2D> {
    protected final Long id;
    protected final Vector2D target;
    protected final Vector2D initial;
    public Strike(Long id, GameRoom gameRoom, boolean isMoving,
                  boolean isAlive, Vector2D position,
                  Vector2D velocity, Vector2D acceleration, Vector2D target, Vector2D initial) {
        super(gameRoom, isMoving, isAlive, position, velocity, acceleration);
        this.id = id;
        this.target = target;
        this.initial = initial;
    }

    public Strike(Long id, GameRoom gameRoom, Vector2D target, Vector2D initial) {
        super(gameRoom);
        this.id = id;
        this.target = target;
        this.initial = initial;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public InitPack getInitPack() {
        return new StrikeInitPack(id, position);
    }

    @Override
    public UpdatePack getUpdatePack() {
        return new StrikeUpdatePack(id, position);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrikeInitPack implements InitPack {
        long id;
        Vector2D position;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrikeUpdatePack implements UpdatePack {
        long id;
        Vector2D position;
    }
}
