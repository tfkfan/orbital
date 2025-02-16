package com.tfkfan.vertx.network.pack.init;

import com.tfkfan.vertx.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInitPack implements InitPack {
    private PlayerInitPack player;
    private long loopRate;
    private long playersCount;
    private Collection<PlayerInitPack> players;
}
