package io.github.tfkfan.orbital.core.network.message;

import io.vertx.core.json.jackson.DatabindCodec;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int type;
    private Map<String, Object> data;

    public Message(int type) {
        this.type = type;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return DatabindCodec.mapper().writeValueAsString(this);
    }
}
