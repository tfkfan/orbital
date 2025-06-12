package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ServerConfig {
    private int port;
    private Integer roomVerticleInstances;

    public ServerConfig() {
    }

    public ServerConfig(int port, Integer roomVerticleInstances) {
        this.port = port;
        this.roomVerticleInstances = roomVerticleInstances;
    }

    public int getPort() {
        return port;
    }

    public ServerConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public Integer getRoomVerticleInstances() {
        return roomVerticleInstances;
    }

    public ServerConfig setRoomVerticleInstances(Integer roomVerticleInstances) {
        this.roomVerticleInstances = roomVerticleInstances;
        return this;
    }
}

