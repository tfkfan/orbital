package io.github.tfkfan.orbital.core.network.message;

import java.util.Map;

public class Message {
    private int type;
    private Map<String, Object> data;

    public Message(int type) {
        this.type = type;
    }

    public Message() {
    }

    public Message(int type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Message setType(int type) {
        this.type = type;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Message setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
