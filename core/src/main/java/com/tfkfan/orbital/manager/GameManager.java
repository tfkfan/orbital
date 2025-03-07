package com.tfkfan.orbital.manager;

import com.tfkfan.orbital.room.GameRoom;

public interface GameManager {
    void onBattleEnd(GameRoom room);
    void onBattleStart(GameRoom room);
}
