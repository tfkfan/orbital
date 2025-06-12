package io.github.tfkfan.orbital.core.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.tfkfan.orbital.core.session.GatewaySession;
import io.vertx.core.json.JsonObject;

import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoomJoinEvent extends AbstractEvent {
    private GatewaySession session;
    private JsonObject data;

    public GameRoomJoinEvent() {
    }

    public GameRoomJoinEvent(GatewaySession session, JsonObject data) {
        this.session = session;
        this.data = data;
    }

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

    public GatewaySession getSession() {
        return session;
    }

    public GameRoomJoinEvent setSession(GatewaySession session) {
        this.session = session;
        return this;
    }

    public JsonObject getData() {
        return data;
    }

    public GameRoomJoinEvent setData(JsonObject data) {
        this.data = data;
        return this;
    }
}