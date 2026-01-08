package io.github.tfkfan.orbital.core.model;

import io.github.tfkfan.orbital.core.Initializable;
import io.github.tfkfan.orbital.core.Updatable;
import io.github.tfkfan.orbital.core.network.pack.IInitPackProvider;
import io.github.tfkfan.orbital.core.network.pack.IUpdatePackProvider;
import io.github.tfkfan.orbital.core.room.GameRoom;

import java.io.Serializable;

public interface GameEntity<I extends Serializable> extends Entity<I>, Updatable, Initializable, IUpdatePackProvider, IInitPackProvider {
    boolean isMoving();

    boolean isAlive();

    default boolean isDead() {
        return !isAlive();
    }

    default void init() {

    }

    GameRoom getGameRoom();

    <R extends GameRoom> R getGameRoom(Class<R> roomClass);
}
