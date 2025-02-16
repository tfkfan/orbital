package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.math.Vector;
import com.tfkfan.vertx.network.pack.UpdatePack;
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
    protected Vector position = new Vector();
    protected Vector velocity = new Vector();
    protected Vector acceleration = new Vector();
}
