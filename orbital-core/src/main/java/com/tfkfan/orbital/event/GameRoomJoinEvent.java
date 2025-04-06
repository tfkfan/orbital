package com.tfkfan.orbital.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfkfan.orbital.session.GatewaySession;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoomJoinEvent extends AbstractEvent {
    private GatewaySession session;
    private JsonObject data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoomJoinEvent that = (GameRoomJoinEvent) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(session);
    }
}