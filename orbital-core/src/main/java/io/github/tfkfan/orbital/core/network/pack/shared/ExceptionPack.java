package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.Pack;

public class ExceptionPack implements Pack {
    private String message;

    public ExceptionPack() {
    }

    public ExceptionPack(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionPack setMessage(String message) {
        this.message = message;
        return this;
    }
}
