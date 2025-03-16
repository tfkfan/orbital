package com.tfkfan.orbital.model;

import com.tfkfan.orbital.network.pack.PrivateUpdatePack;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.network.pack.update.GameUpdatePack;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class DefaultGameUpdatePack extends GameUpdatePack {
    private Collection<UpdatePack> strikes;

    public DefaultGameUpdatePack(PrivateUpdatePack player, Collection<UpdatePack> players, Collection<UpdatePack> strikes) {
        super(player, players);
        this.strikes = strikes;
    }
}
