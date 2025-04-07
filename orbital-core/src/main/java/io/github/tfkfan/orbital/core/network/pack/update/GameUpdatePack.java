package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdatePack implements UpdatePack {
    private PrivateUpdatePack player;
    private Collection<UpdatePack> players;
}
