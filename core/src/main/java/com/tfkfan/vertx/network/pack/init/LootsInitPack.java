package com.tfkfan.vertx.network.pack.init;

import com.tfkfan.vertx.network.pack.InitPack;
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
