package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;

import java.util.Collection;

public class BaseGameUpdatePack implements UpdatePack {
    private PrivateUpdatePack player;
    private Collection<UpdatePack> players;

    public BaseGameUpdatePack() {
    }

    public BaseGameUpdatePack(PrivateUpdatePack player, Collection<UpdatePack> players) {
        this.player = player;
        this.players = players;
    }

    public PrivateUpdatePack getPlayer() {
        return player;
    }

    public BaseGameUpdatePack setPlayer(PrivateUpdatePack player) {
        this.player = player;
        return this;
    }

    public Collection<UpdatePack> getPlayers() {
        return players;
    }

    public BaseGameUpdatePack setPlayers(Collection<UpdatePack> players) {
        this.players = players;
        return this;
    }
}
