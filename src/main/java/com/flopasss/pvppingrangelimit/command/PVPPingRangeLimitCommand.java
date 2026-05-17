package com.flopasss.pvppingrangelimit.command;

import static net.minecraft.commands.Commands.literal;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.flopasss.pvppingrangelimit.util.DataHolder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
                                FloatArgumentType
                                    .floatArg(0.0f) // Minimum value of 0ms
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
                                FloatArgumentType
                                    .floatArg(0.0f, 1.0f) // Minimum value of 0.0 and maximum value of 1.0
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
                                IntegerArgumentType
                                    .integer(0) // Minimum value of 0 seconds
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
                .then(
                    literal("check").then(
                        Commands.argument(
                            "player",
                            EntityArgument.player()
                        ).executes(context -> {
                            ServerPlayer target = EntityArgument.getPlayer(
                                context,
                                "player"
                            ); // The player being checked is considered the target for this check
                            ServerPlayer attacker = context
                                .getSource()
                                .getPlayerOrException(); // The player executing the command is considered the attacker for this check

                            // Get the executing player smoothed ping
                            DataHolder attackerHolder = (DataHolder) attacker;
                            float attackerPing =
                                attackerHolder.pprl$getSmoothedPing();

                            // Show the executing player their own smoothed ping if they are checking themselves
                            if (target == attacker) {
                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "Your smoothed ping is: " +
                                                    String.format(
                                                        "%.1f",
                                                        attackerPing
                                                    ) +
                                                    "ms"
                                            ),
                                        false
                                    );
                                return 1;
                            }

                            // Get the target smoothed ping
                            DataHolder targetHolder = (DataHolder) target;
                            float targetPing =
                                targetHolder.pprl$getSmoothedPing();

                            // Get the absolute ping difference
                            float pingDiff = Math.abs(
                                attackerPing - targetPing
                            );

                            // Check if the ping difference is within the allowed range
                            boolean canFight =
                                pingDiff <=
                                PVPPingRangeLimit.CONFIG.maxPingDiff;

                            // Send a system message to the executing player with the ping check results
                            context
                                .getSource()
                                .sendSystemMessage(
                                    Component.literal(
                                        "§7--- Ping Check ---" +
                                            "\n§7Your Ping: §f" +
                                            String.format(
                                                "%.1f",
                                                attackerPing
                                            ) +
                                            "ms" +
                                            "\n§7" +
                                            target.getScoreboardName() +
                                            "'s Ping: §f" +
                                            String.format("%.1f", targetPing) +
                                            "ms" +
                                            "\n§7Difference: §f" +
                                            String.format("%.1f", pingDiff) +
                                            "ms" +
                                            "\n§7Status: " +
                                            (canFight
                                                ? "§aCAN FIGHT"
                                                : "§cCANCELLED")
                                    )
                                );
                            return 1;
                        })
                    )
                )
        );
    }
}
