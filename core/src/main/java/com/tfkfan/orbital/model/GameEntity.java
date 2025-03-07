package com.tfkfan.orbital.model;

import com.tfkfan.orbital.Initializable;
import com.tfkfan.orbital.Updatable;
import com.tfkfan.orbital.network.pack.IInitPackProvider;
import com.tfkfan.orbital.network.pack.IUpdatePackProvider;
import com.tfkfan.orbital.room.GameRoom;

public interface GameEntity<I> extends Updatable, Initializable, IUpdatePackProvider, IInitPackProvider {
    I getId();

    boolean isMoving();

    boolean isAlive();

    GameRoom getGameRoom();

    <R extends GameRoom> R getGameRoom(Class<R> roomClass);
}
