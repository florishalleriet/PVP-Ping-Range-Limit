package com.flopasss.pvppingrangelimit.command;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.flopasss.pvppingrangelimit.data.PVPPingRangeLimitPlayerData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class PVPPingRangeLimitCommand {

    public static void register(
        CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
            literal("pvpPingRangeLimit")
                // Config boolean toggles
                .then(
                    literal("enabled")
                        .requires(
                            Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)
                        )
                        .then(
                            Commands.argument(
                                "boolean",
                                BoolArgumentType.bool()
                            ).executes(context -> {
                                boolean bool = BoolArgumentType.getBool(
                                    context,
                                    "boolean"
                                );

                                // Update config via the setter
                                PVPPingRangeLimit.CONFIG.enabled = bool;
                                PVPPingRangeLimit.CONFIG.save();

                                // Send feedback using the factory
                                context
                                    .getSource()
                                    .sendSuccess(
                                        () ->
                                            Component.literal(
                                                "The mod is now " +
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
        );
    }
}
