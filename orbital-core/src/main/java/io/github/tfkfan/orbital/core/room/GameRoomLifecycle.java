package io.github.tfkfan.orbital.core.room;

public interface GameRoomLifecycle {
    void start();
    void create();

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

    interface GameRoomLifecycleHandler {
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
}
