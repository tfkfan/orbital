package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.InitPack;


public class GameMessagePack implements InitPack {
    private Integer messageType;
    private String message;

    public GameMessagePack() {
    }

    public GameMessagePack(Integer messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public GameMessagePack setMessageType(Integer messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GameMessagePack setMessage(String message) {
        this.message = message;
        return this;
    }
}
