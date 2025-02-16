package com.tfkfan.vertx.network.pack.shared;

import com.tfkfan.vertx.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerKillPack implements InitPack {
    private long killedId;
    private long killerId;
    private String killedName;
    private String killerName;
    private long playersCount;
}
