package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.tfkfan.orbital.core.configuration.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class RoomConfig {
    private Long loopRate = Constants.DEFAULT_LOOPRATE;
    private Long initDelay = Constants.ROOM_INIT_DELAY;
    private Long startDelay = Constants.ROOM_START_DELAY;
    private Long endDelay = Constants.ROOM_END_DELAY;
    private Integer maxPlayers = Constants.MAX_PLAYERS;

    public RoomConfig() {
    }

    public RoomConfig(Long loopRate, Long initDelay, Long startDelay, Long endDelay, Integer maxPlayers) {
        this.loopRate = loopRate;
        this.initDelay = initDelay;
        this.startDelay = startDelay;
        this.endDelay = endDelay;
        this.maxPlayers = maxPlayers;
    }

    public Long getLoopRate() {
        return loopRate;
    }

    public RoomConfig setLoopRate(Long loopRate) {
        this.loopRate = loopRate;
        return this;
    }

    public Long getInitDelay() {
        return initDelay;
    }

    public RoomConfig setInitDelay(Long initDelay) {
        this.initDelay = initDelay;
        return this;
    }

    public Long getStartDelay() {
        return startDelay;
    }

    public RoomConfig setStartDelay(Long startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    public Long getEndDelay() {
        return endDelay;
    }

    public RoomConfig setEndDelay(Long endDelay) {
        this.endDelay = endDelay;
        return this;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public RoomConfig setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }
}
