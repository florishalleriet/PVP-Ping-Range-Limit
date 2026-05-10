package com.flopasss.pvppingrangelimit.mixin;

import com.flopasss.pvppingrangelimit.util.PingDataHolder;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements PingDataHolder {

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
}
