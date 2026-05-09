package com.flopasss.pvppingrangelimit;

import com.flopasss.pvppingrangelimit.command.PVPPingRangeLimitCommand;
import com.flopasss.pvppingrangelimit.config.PVPPingRangeLimitConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PVPPingRangeLimit implements ModInitializer {

    public static final String MOD_ID = "pvp-ping-range-limit";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static PVPPingRangeLimitConfig CONFIG;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Load the config when the mod initializes
        CONFIG = PVPPingRangeLimitConfig.load();

        // Register the command
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                PVPPingRangeLimitCommand.register(dispatcher)
        );

        LOGGER.info("Flopasss PVP Ping Range Limit initialized");
    }
}
