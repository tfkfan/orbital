package com.tfkfan.orbital.network.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int type;
    private Map<String, Object> data;

    public Message(int type) {
        this.type = type;
    }
}
