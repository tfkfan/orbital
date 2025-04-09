package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.Initializable;
import io.github.tfkfan.orbital.core.Updatable;
import io.github.tfkfan.orbital.core.network.pack.IInitPackProvider;
import io.github.tfkfan.orbital.core.network.pack.IUpdatePackProvider;
import io.github.tfkfan.orbital.core.room.GameRoom;

public interface GameEntity<I> extends Updatable, Initializable, IUpdatePackProvider, IInitPackProvider {
    I getId();

    boolean isMoving();

    boolean isAlive();

    default boolean isDead(){
        return !isAlive();
    }

    GameRoom getGameRoom();

    <R extends GameRoom> R getGameRoom(Class<R> roomClass);
}
