package com.flopasss.pvppingrangelimit.util;

public interface DataHolder {
    // Smooted ping
    float getSmoothedPing();
    void setSmoothedPing(float ping);

    // Message cooldown
    long getLastMessageTime();
    void setLastMessageTime(long time);
}
