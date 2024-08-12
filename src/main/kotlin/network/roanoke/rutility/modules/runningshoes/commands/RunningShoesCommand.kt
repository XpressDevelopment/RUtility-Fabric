package network.roanoke.rutility.modules.runningshoes.commands

import com.mojang.brigadier.arguments.StringArgumentType
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.modules.runningshoes.RunningShoes
import network.roanoke.rutility.modules.runningshoes.items.ShoesItem

class RunningShoesCommand(private val module: RunningShoes) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("runningshoes").requires(Permissions.require("rutility.runningshoes", 2))
                    .then(
                        CommandManager.literal("give").requires(Permissions.require("rutility.runningshoes.give", 2))
                            .then(CommandManager.argument("type", StringArgumentType.string())
                                .executes { ctx ->
                                    if (module.isEnabled()) {
                                        if (ctx.source.player != null) {
                                            val player = ctx.source.player as ServerPlayerEntity
                                            val type = StringArgumentType.getString(ctx, "type")
                                            if (type != "old" && type != "regular") {
                                                ctx.source.sendFeedback({ Text.literal("§cInvalid type. Must be 'old' or 'regular'") }, false)
                                                return@executes 1
                                            }
                                            if (type == "old")
                                                player.giveItemStack(ShoesItem.getOldRunningShoes())
                                            else
                                                player.giveItemStack(ShoesItem.getRunningShoes())
                                            ctx.source.sendFeedback({ Text.literal("§aGave Running Shoes to ").append(ctx.source.displayName) }, true)
                                        } else
                                            ctx.source.sendFeedback({ Text.literal("§cYou must be a player to use this command") }, false)
                                    } else {
                                        ctx.source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                                    }
                                    1
                                }
                            )
                    )
            )
        })
    }

}