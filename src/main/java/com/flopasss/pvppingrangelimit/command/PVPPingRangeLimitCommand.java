package com.flopasss.pvppingrangelimit.command;

import static net.minecraft.commands.Commands.literal;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PVPPingRangeLimitCommand {

    public static void register(
        CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
            literal("pvpPingRangeLimit")
                .then(
                    literal("enabled")
                        .requires(
                            Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)
                        )
                        .executes(context -> {
                            context
                                .getSource()
                                .sendSuccess(
                                    () ->
                                        Component.literal(
                                            "Current mod is: " +
                                                (PVPPingRangeLimit.CONFIG.enabled
                                                    ? "enabled"
                                                    : "disabled")
                                        ),
                                    false
                                );

                            return 1;
                        })
                        .then(
                            Commands.argument(
                                "boolean",
                                BoolArgumentType.bool()
                            ).executes(context -> {
                                boolean bool = BoolArgumentType.getBool(
                                    context,
                                    "boolean"
                                );

                                PVPPingRangeLimit.CONFIG.enabled = bool;
                                PVPPingRangeLimit.CONFIG.save();

                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "Set mod to: " +
                                                    (bool
                                                        ? "enabled"
                                                        : "disabled")
                                            ),
                                        true
                                    );
                                return 1;
                            })
                        )
                )
                .then(
                    literal("maxPingDiff")
                        .requires(
                            Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)
                        )
                        .executes(context -> {
                            context
                                .getSource()
                                .sendSuccess(
                                    () ->
                                        Component.literal(
                                            "Current maximum ping difference is: " +
                                                PVPPingRangeLimit.CONFIG.maxPingDiff +
                                                "ms"
                                        ),
                                    false
                                );

                            return 1;
                        })
                        .then(
                            Commands.argument(
                                "float",
                                FloatArgumentType.floatArg(0.0f) // Minimum value of 0ms
                            ).executes(context -> {
                                float value = FloatArgumentType.getFloat(
                                    context,
                                    "float"
                                );

                                PVPPingRangeLimit.CONFIG.maxPingDiff = value;
                                PVPPingRangeLimit.CONFIG.save();

                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "Set maximum ping difference to: " +
                                                    value +
                                                    "ms"
                                            ),
                                        true
                                    );

                                return 1;
                            })
                        )
                )
                .then(
                    literal("alphaSmoothingFactor")
                        .requires(
                            Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)
                        )
                        .executes(context -> {
                            context
                                .getSource()
                                .sendSuccess(
                                    () ->
                                        Component.literal(
                                            "Current alpha smoothing factor is: " +
                                                PVPPingRangeLimit.CONFIG.alphaSmoothingFactor
                                        ),
                                    false
                                );

                            return 1;
                        })
                        .then(
                            Commands.argument(
                                "float",
                                FloatArgumentType.floatArg(0.0f, 1.0f) // Minimum value of 0.0 and maximum value of 1.0
                            ).executes(context -> {
                                float value = FloatArgumentType.getFloat(
                                    context,
                                    "float"
                                );

                                PVPPingRangeLimit.CONFIG.alphaSmoothingFactor =
                                    value;
                                PVPPingRangeLimit.CONFIG.save();

                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "Set alpha smoothing factor to: " +
                                                    value
                                            ),
                                        true
                                    );

                                return 1;
                            })
                        )
                )
                .then(
                    literal("messageCooldown")
                        .requires(
                            Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)
                        )
                        .executes(context -> {
                            context
                                .getSource()
                                .sendSuccess(
                                    () ->
                                        Component.literal(
                                            "Current message cooldown is: " +
                                                PVPPingRangeLimit.CONFIG.messageCooldown +
                                                " seconds"
                                        ),
                                    false
                                );

                            return 1;
                        })
                        .then(
                            Commands.argument(
                                "int",
                                IntegerArgumentType.integer(0) // Minimum value of 0 seconds
                            ).executes(context -> {
                                int value = IntegerArgumentType.getInteger(
                                    context,
                                    "int"
                                );

                                PVPPingRangeLimit.CONFIG.messageCooldown =
                                    value;
                                PVPPingRangeLimit.CONFIG.save();

                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "Set message cooldown to: " +
                                                    value +
                                                    "s"
                                            ),
                                        true
                                    );

                                return 1;
                            })
                        )
                )
        );
    }
}
