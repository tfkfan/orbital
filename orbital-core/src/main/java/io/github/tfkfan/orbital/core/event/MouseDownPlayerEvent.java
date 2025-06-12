package io.github.tfkfan.orbital.core.event;

import io.github.tfkfan.orbital.core.math.Vector2D;

public class MouseDownPlayerEvent extends AbstractEvent{
    private String key;
    private Vector2D target;

    public MouseDownPlayerEvent() {
    }

    public MouseDownPlayerEvent(String key, Vector2D target) {
        this.key = key;
        this.target = target;
    }

    public String getKey() {
        return key;
    }

    public MouseDownPlayerEvent setKey(String key) {
        this.key = key;
        return this;
    }

    public Vector2D getTarget() {
        return target;
    }

    public MouseDownPlayerEvent setTarget(Vector2D target) {
        this.target = target;
        return this;
    }
}