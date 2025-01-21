package com.tfkfan.webgame.network.pack.init;

import com.tfkfan.webgame.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LootsInitPack implements InitPack{
    Collection<LootInitPack> loots;
}
