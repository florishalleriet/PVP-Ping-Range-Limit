package com.flopasss.pvppingrangelimit.mixin;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.flopasss.pvppingrangelimit.util.DataHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class CombatMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void onHurtServer(
        ServerLevel level,
        DamageSource source,
        float amount,
        CallbackInfoReturnable<Boolean> cir
    ) {
        // Return early if the mod is disabled
        if (!PVPPingRangeLimit.CONFIG.enabled) return;

        // Check if the damage source is a player attacking another player instance. If not, return early.
        if (
            !(source.getEntity() instanceof ServerPlayer attacker &&
                (Object) this instanceof ServerPlayer target)
        ) return;

        // Cast the attacker and target to DataHolder to access smoothed ping data and message cooldown
        DataHolder attackerHolder = (DataHolder) attacker;
        DataHolder targetHolder = (DataHolder) target;

        // Get the smoothed ping for both the attacker and the target
        float attackerPing = attackerHolder.getSmoothedPing();
        float targetPing = targetHolder.getSmoothedPing();

        // Safe-guard against uninitialized smoothed ping values (negative values indicate uninitialized)
        if (attackerPing < 0 || targetPing < 0) return;

        // Calculate the absolute ping difference
        float pingDiff = Math.abs(attackerPing - targetPing);

        // Return early if the ping difference is within the allowed range
        if (pingDiff < PVPPingRangeLimit.CONFIG.maxPingDiff) return;

        // Cancel the attack
        cir.setReturnValue(false);

        // Get the current time and the last message time for the attacker
        long currentTime = System.currentTimeMillis();
        long lastMsgTime = attackerHolder.getLastMessageTime();

        // Return early if the attacker is still on cooldown for receiving messages
        if (
            currentTime - lastMsgTime <
            PVPPingRangeLimit.CONFIG.messageCooldown * 1000
        ) return;

        // Send a system message to the attacker about the attack being canceled due to high ping difference
        attacker.sendSystemMessage(
            Component.literal("§cAttack canceled: ping difference too high")
        );

        // Update the last message time for the attacker to the current time
        attackerHolder.setLastMessageTime(currentTime);
    }
}
