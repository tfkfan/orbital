package com.tfkfan.webgame.network.pack.shared;

import com.tfkfan.webgame.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoPack implements  InitPack {
    private boolean reconnectKeyExists;
}
