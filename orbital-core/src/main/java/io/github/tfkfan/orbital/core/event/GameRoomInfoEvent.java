package io.github.tfkfan.orbital.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameRoomInfoEvent extends AbstractEvent{
    private String reconnectKey;
}
