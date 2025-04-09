package io.github.tfkfan.orbital.core.event;

import io.github.tfkfan.orbital.core.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MouseDownPlayerEvent extends AbstractEvent{
    private String key;
    private Vector2D target;
}