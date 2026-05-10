package com.flopasss.pvppingrangelimit.config;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class PVPPingRangeLimitConfig {

    public boolean enabled = true; // The mod is enabled by default
    public float maxPingDiff = 200.0f; // Default max ping is 200ms
    public float alphaSmoothingFactor = 0.1f; // Default smoothing factor for alpha calculation
    public int messageCooldown = 3; // Default cooldown of 3 seconds between messages

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create(); // For JSON serialization
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve(PVPPingRangeLimit.MOD_ID + ".json"); // Config file path

    public static PVPPingRangeLimitConfig load() {
        // Return default config if file doesn't exist to load
        if (!Files.exists(CONFIG_PATH)) {
            PVPPingRangeLimitConfig defaultConfig =
                new PVPPingRangeLimitConfig();
            defaultConfig.save(); // Save the default config to create the file
            return defaultConfig;
        }

        try {
            // Read the JSON file into a string
            String json = Files.readString(CONFIG_PATH);

            // Deserialize the JSON string into a PVPPingRangeLimitConfig variable
            PVPPingRangeLimitConfig config = GSON.fromJson(
                json,
                PVPPingRangeLimitConfig.class
            );

            // If the config is null, return default config
            if (config == null) return new PVPPingRangeLimitConfig();

            // Return the loaded config
            return config;
        } catch (Exception e) {
            // Log the error
            PVPPingRangeLimit.LOGGER.warn(
                "Failed to load config, using default values",
                e
            );

            // Return default config
            return new PVPPingRangeLimitConfig();
        }
    }

    public void save() {
        try {
            // Ensure the config directory exists
            Files.createDirectories(CONFIG_PATH.getParent());

            // Serialize the config object to a JSON string
            String json = GSON.toJson(this);

            // Write the JSON string to the config file
            Files.writeString(CONFIG_PATH, json);
        } catch (Exception e) {
            // Log the error
            PVPPingRangeLimit.LOGGER.error("Failed to save config", e);
        }
    }
}
