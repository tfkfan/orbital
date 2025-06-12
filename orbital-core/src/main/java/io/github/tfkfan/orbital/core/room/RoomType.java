package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.Fields;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public enum RoomType {
    BATTLE_ROYALE, TRAINING, CUSTOM;

    public static RoomType getOrDefault(JsonObject data, RoomType defaultValue) {
        final String rtv = Objects.requireNonNull(data).getString(Fields.roomType);
        return rtv != null ? RoomType.valueOf(rtv.toUpperCase()) : defaultValue;
    }
}
