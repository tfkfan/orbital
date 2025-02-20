package com.tfkfan.orbital.game.model;

import com.tfkfan.orbital.game.room.GameRoom;
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
}
