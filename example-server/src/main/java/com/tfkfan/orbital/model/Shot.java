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
public class Shot extends BaseGameEntity<Long, Vector2D> {
    protected final Long id;
    protected final Vector2D target;
    protected final Vector2D initial;

    public Shot(Long id, GameRoom gameRoom, Vector2D target, Vector2D initial) {
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
        return new ShotInitPack(id, position);
    }

    @Override
    public UpdatePack getUpdatePack() {
        return new ShotUpdatePack(id, position);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShotInitPack implements InitPack {
        long id;
        Vector2D position;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShotUpdatePack implements UpdatePack {
        long id;
        Vector2D position;
    }
}
