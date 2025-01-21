package com.tfkfan.webgame.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfkfan.webgame.config.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomProperties {
    private Long roomVerticleInstances;
    private Long loopRate = Constants.DEFAULT_LOOPRATE;
    private Long initDelay = Constants.ROOM_INIT_DELAY;
    private Long startDelay = Constants.ROOM_START_DELAY;
    private Long endDelay = Constants.ROOM_END_DELAY;
    private Long maxPlayers = Constants.MAX_PLAYERS;
}
