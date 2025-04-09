package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRoomInfoPack implements InitPack {
    private long timestamp;
}
