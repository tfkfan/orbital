package com.tfkfan.orbital.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfkfan.orbital.configuration.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomConfig {
    private Integer instances;
    private Long loopRate = Constants.DEFAULT_LOOPRATE;
    private Long initDelay = Constants.ROOM_INIT_DELAY;
    private Long startDelay = Constants.ROOM_START_DELAY;
    private Long endDelay = Constants.ROOM_END_DELAY;
    private Integer maxPlayers = Constants.MAX_PLAYERS;

    public RoomConfig(Integer instances) {
        this.instances = instances;
    }
}
