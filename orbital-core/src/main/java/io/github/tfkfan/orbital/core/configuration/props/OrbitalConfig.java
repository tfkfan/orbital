package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrbitalConfig {
    private ServerConfig server;
    private RoomConfig room;

    public OrbitalConfig() {
    }

    public OrbitalConfig(ServerConfig server, RoomConfig room) {
        this.server = server;
        this.room = room;
    }

    public ServerConfig getServer() {
        return server;
    }

    public OrbitalConfig setServer(ServerConfig server) {
        this.server = server;
        return this;
    }

    public RoomConfig getRoom() {
        return room;
    }

    public OrbitalConfig setRoom(RoomConfig room) {
        this.room = room;
        return this;
    }
}
