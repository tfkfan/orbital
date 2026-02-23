package io.github.tfkfan.orbital.core.network.message;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.SneakyThrows;

import java.util.Map;

public class Message {
    private final int type;
    private final long timestamp;
    private Map<String, Object> data;

    public Message(int type) {
        this(type, null);
    }

    public Message(int type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return DatabindCodec.mapper().writeValueAsString(this);
    }
}
