package io.github.tfkfan.orbital.core.room;

public interface GameRoomLifecycle {
    default void onCreate() {
    }

    default void onStart() {
    }

    default void onBattleStart() {
    }

    default void onBattleEnd() {
    }

    default void onDestroy() {
    }
}
