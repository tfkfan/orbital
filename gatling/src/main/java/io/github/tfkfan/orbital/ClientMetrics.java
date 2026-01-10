package io.github.tfkfan.orbital;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientMetrics {
    private Integer updateTicks = 0;
    private long lastUpdateTime = 0L;
    private long lastJoinRequestTime = 0L;
    private long lastJoinSuccessTime = 0L;
    private long lastBattleStartTime = 0L;

    private long lastUpdateDelay = 0L;
    private long sumUpdateDelay = 0L;
    private long maxUpdateDelay = 0L;
    private long averageUpdateDelay = 0L;
}
