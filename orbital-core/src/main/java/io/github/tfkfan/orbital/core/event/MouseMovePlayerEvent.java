package io.github.tfkfan.orbital.core.event;

import io.github.tfkfan.orbital.core.math.Vector2D;

public class MouseMovePlayerEvent extends AbstractEvent{
    private Vector2D target;

    public MouseMovePlayerEvent() {
    }

    public MouseMovePlayerEvent(Vector2D target) {
        this.target = target;
    }

    public Vector2D getTarget() {
        return target;
    }

    public MouseMovePlayerEvent setTarget(Vector2D target) {
        this.target = target;
        return this;
    }
}
