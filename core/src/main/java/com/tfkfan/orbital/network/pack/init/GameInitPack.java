package com.tfkfan.orbital.network.pack.init;

import com.tfkfan.orbital.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInitPack implements InitPack {
    private InitPack player;
    private long loopRate;
    private long playersCount;
    private Collection<InitPack> players;
}
