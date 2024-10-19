package network.roanoke.rutility.modules.runningshoes.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.runningshoes.RunningShoes
import network.roanoke.rutility.modules.runningshoes.items.ShoesItem
import network.roanoke.rutility.utils.Utils
import java.util.concurrent.CompletableFuture

class RunningShoesCommand(private val module: RunningShoes) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("runningshoes").requires(Permissions.require("rutility.runningshoes", 2))
                    .then(
                        CommandManager.literal("give").requires(Permissions.require("rutility.runningshoes.give", 2))
                            .then(CommandManager.argument("type", StringArgumentType.string())
                                .suggests(this::suggestTypes)
                                .then(CommandManager.argument("player", StringArgumentType.string())
                                    .suggests(this::suggestPlayers)
                                    .executes { ctx ->
                                        if (module.isEnabled()) {
                                            val player = Utils.getPlayerByName(StringArgumentType.getString(ctx, "player")) ?: return@executes 1
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
                                        } else {
                                            ctx.source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                                        }
                                        1
                                    }
                                )
                            )
                    )
            )
        })
    }

    private fun suggestPlayers(
        ctx: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val playerNames = Utils.getAllPlayerNames()
        return CommandSource.suggestMatching(playerNames!!, builder)
    }

    private fun suggestTypes(
        ctx: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val types = listOf("old", "regular")
        return CommandSource.suggestMatching(types, builder)
    }

}