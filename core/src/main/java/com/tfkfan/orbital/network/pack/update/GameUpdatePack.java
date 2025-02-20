package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.network.pack.PrivateUpdatePack;
import com.tfkfan.orbital.network.pack.UpdatePack;
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
