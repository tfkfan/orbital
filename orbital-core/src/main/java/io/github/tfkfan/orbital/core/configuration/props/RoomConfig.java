package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.tfkfan.orbital.core.configuration.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RoomConfig {
    private Long loopRate = Constants.DEFAULT_LOOPRATE;
    private Long startDelay;
    private Long matchDuration;
    private Integer maxPlayers;
}
