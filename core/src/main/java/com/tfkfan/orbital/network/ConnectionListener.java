package com.tfkfan.orbital.network;

public interface ConnectionListener<T> {
    void onDisconnect(T data);

    void onConnect(T data);
}
