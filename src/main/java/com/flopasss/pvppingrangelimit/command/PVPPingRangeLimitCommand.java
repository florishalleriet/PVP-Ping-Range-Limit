package com.flopasss.pvppingrangelimit.command;

import static net.minecraft.commands.Commands.literal;

import com.flopasss.pvppingrangelimit.PVPPingRangeLimit;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

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
