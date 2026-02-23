package io.github.tfkfan.orbital.data;

import io.github.tfkfan.orbital.core.network.message.Message;

public record MessageWrapper(Message message, long clientTimestamp) {
}
