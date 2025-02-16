package com.tfkfan.vertx.network.message;

import lombok.Getter;

@Getter
public enum MessageType {
    SYSTEM(1), ROOM(2), PRIVATE(3), ADMIN(4);
    private final int type;

    MessageType(int type) {
        this.type = type;
    }
}
