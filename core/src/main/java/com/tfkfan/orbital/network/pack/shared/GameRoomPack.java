package com.tfkfan.orbital.network.pack.shared;

import com.tfkfan.orbital.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameRoomPack implements InitPack {
    private long timestamp;
}
