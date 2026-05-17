package com.flopasss.pvppingrangelimit.util;

public interface DataHolder {
    // Smooted ping
    float pprl$getSmoothedPing();
    void pprl$setSmoothedPing(float ping);

    // Initialized
    boolean pprl$isSmoothedPingInitialized();
    void pprl$setSmoothedPingInitialized(boolean initialized);

    // Message cooldown
    long pprl$getLastMessageTime();
    void pprl$setLastMessageTime(long time);
}
