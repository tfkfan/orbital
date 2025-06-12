package io.github.tfkfan.orbital.core.network.pack.init;

import io.github.tfkfan.orbital.core.network.pack.InitPack;

import java.util.Collection;

public class BaseGameInitPack implements InitPack {
    private InitPack player;
    private long loopRate;
    private long playersCount;
    private Collection<InitPack> players;

    public BaseGameInitPack() {
    }

    public BaseGameInitPack(InitPack player, long loopRate, long playersCount, Collection<InitPack> players) {
        this.player = player;
        this.loopRate = loopRate;
        this.playersCount = playersCount;
        this.players = players;
    }

    public InitPack getPlayer() {
        return player;
    }

    public BaseGameInitPack setPlayer(InitPack player) {
        this.player = player;
        return this;
    }

    public long getLoopRate() {
        return loopRate;
    }

    public BaseGameInitPack setLoopRate(long loopRate) {
        this.loopRate = loopRate;
        return this;
    }

    public long getPlayersCount() {
        return playersCount;
    }

    public BaseGameInitPack setPlayersCount(long playersCount) {
        this.playersCount = playersCount;
        return this;
    }

    public Collection<InitPack> getPlayers() {
        return players;
    }

    public BaseGameInitPack setPlayers(Collection<InitPack> players) {
        this.players = players;
        return this;
    }
}
