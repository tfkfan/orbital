package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseGameInitPack implements InitPack {
    private InitPack player;
    private long loopRate;
    private long playersCount;
    private Collection<InitPack> players;
}
