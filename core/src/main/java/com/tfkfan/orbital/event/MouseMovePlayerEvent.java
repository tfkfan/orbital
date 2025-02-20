package com.tfkfan.orbital.event;

import com.tfkfan.orbital.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouseMovePlayerEvent extends AbstractEvent{
    private Vector2D target;
}
