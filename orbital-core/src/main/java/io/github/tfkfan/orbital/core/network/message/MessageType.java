package io.github.tfkfan.orbital.core.network.message;

public enum MessageType {
    SYSTEM(1), ROOM(2), PRIVATE(3), ADMIN(4);
    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
