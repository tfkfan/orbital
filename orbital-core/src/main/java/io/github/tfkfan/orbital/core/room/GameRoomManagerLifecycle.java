package io.github.tfkfan.orbital.core.room;

public interface GameRoomManagerLifecycle {
    default void onCreate(GameRoom room) {
    }

    default void onStart(GameRoom room) {
    }

    default void onBattleStart(GameRoom room) {
    }

    default void onBattleEnd(GameRoom room) {
    }

    default void onDestroy(GameRoom room) {
    }
}
