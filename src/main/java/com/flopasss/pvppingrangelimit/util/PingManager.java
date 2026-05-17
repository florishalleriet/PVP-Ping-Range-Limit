package com.flopasss.pvppingrangelimit.util;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class PingManager {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Update smoothed ping for all players every second (20 ticks)
            if (!(server.getTickCount() % 20 == 0)) return;

            // Return early if the mod is disabled
            if (!PVPPingRangeLimit.CONFIG.enabled) return;

            // Calculate smoothed ping for each player
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                updateSmoothedPing(player);
            }
        });
    }

    private static void updateSmoothedPing(ServerPlayer player) {
        // Cast the player to PingDataHolder to access smoothed ping data
        DataHolder holder = (DataHolder) player;

        // Get the current ping and the old smoothed ping
        float currentPing = (float) player.connection.latency();

        // If the smoothed ping is not initialized, set it to the current ping
        if (!holder.pprl$isSmoothedPingInitialized()) {
            holder.pprl$setSmoothedPing(currentPing);

            // Mark as initialized if we have a ping value greater than 0
            if (currentPing > 0.0f) holder.pprl$setSmoothedPingInitialized(
                true
            );

            return;
        }

        // Calculate the new smoothed ping using exponential moving average
        float oldEma = holder.pprl$getSmoothedPing();
        float alpha = PVPPingRangeLimit.CONFIG.alphaSmoothingFactor;
        float newEma = (alpha * currentPing) + ((1.0f - alpha) * oldEma);

        // Update the smoothed ping in the holder
        holder.pprl$setSmoothedPing(newEma);
    }
}
