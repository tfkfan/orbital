package com.tfkfan.orbital.model;

import com.tfkfan.orbital.network.pack.PrivateUpdatePack;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.network.pack.update.GameUpdatePack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DefaultGameUpdatePack extends GameUpdatePack {
    private Collection<UpdatePack> shots;

    public DefaultGameUpdatePack(PrivateUpdatePack player, Collection<UpdatePack> players, Collection<UpdatePack> shots) {
        super(player, players);
        this.shots = shots;
    }
}
