package io.github.tfkfan.orbital.core.network;

public interface ConnectionListener<T> {
    void onDisconnect(T data);

    void onConnect(T data);
}
