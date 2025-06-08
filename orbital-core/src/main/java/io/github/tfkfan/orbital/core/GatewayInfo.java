package io.github.tfkfan.orbital.core;

import java.io.Serializable;

public record GatewayInfo(String nodeId, String address, int port) implements Serializable {
}