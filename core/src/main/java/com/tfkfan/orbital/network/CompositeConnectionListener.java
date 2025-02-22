package com.tfkfan.orbital.network;

public interface CompositeConnectionListener {
    VerticleListener getVerticleListener();
    SessionListener getSessionListener();
}
