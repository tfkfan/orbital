package com.tfkfan.orbital.network.pack.shared;

import com.tfkfan.orbital.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRoomInfoPack implements InitPack {
    private long timestamp;
}
