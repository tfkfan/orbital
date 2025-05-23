package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.tfkfan.orbital.core.configuration.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RoomConfig {
    private Long loopRate = Constants.DEFAULT_LOOPRATE;
    private Long initDelay = Constants.ROOM_INIT_DELAY;
    private Long startDelay = Constants.ROOM_START_DELAY;
    private Long endDelay = Constants.ROOM_END_DELAY;
    private Integer maxPlayers = Constants.MAX_PLAYERS;
}
