package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdatePack implements UpdatePack {
    private PrivatePlayerUpdatePack player;
    private Collection<PlayerUpdatePack> players;
   // private Collection<StrikeUpdatePack> strikes;
  //  private Collection<LootUpdatePack> loots;
}
