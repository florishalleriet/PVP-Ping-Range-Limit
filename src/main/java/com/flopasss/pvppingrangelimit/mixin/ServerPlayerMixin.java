package com.flopasss.pvppingrangelimit.mixin;

import com.flopasss.pvppingrangelimit.util.DataHolder;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements DataHolder {

    // Smoothed ping
    @Unique
    private float smoothedPing = -1.0f;

    @Override
    public float pprl$getSmoothedPing() {
        return this.smoothedPing;
    }

    @Override
    public void pprl$setSmoothedPing(float ping) {
        this.smoothedPing = ping;
    }

    // Message cooldown
    @Unique
    private long lastMessageTime = 0L;

    @Override
    public long pprl$getLastMessageTime() {
        return this.lastMessageTime;
    }

    @Override
    public void pprl$setLastMessageTime(long time) {
        this.lastMessageTime = time;
    }
}
