package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.InitPack;

public class GameSettingsPack implements InitPack{
    private long loopRate;

    public GameSettingsPack() {
    }

    public GameSettingsPack(long loopRate) {
        this.loopRate = loopRate;
    }

    public long getLoopRate() {
        return loopRate;
    }

    public GameSettingsPack setLoopRate(long loopRate) {
        this.loopRate = loopRate;
        return this;
    }
}
