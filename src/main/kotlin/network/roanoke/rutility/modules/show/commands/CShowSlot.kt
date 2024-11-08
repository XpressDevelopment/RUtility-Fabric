package network.roanoke.rutility.modules.show.commands

import com.cobblemon.mod.common.Cobblemon
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.modules.show.ShowSlot
import network.roanoke.rutility.utils.ShowUtils
import java.util.function.Consumer

class CShowSlot(private val module: ShowSlot) {
    // Permission node for the Show Slot command.
    val SHOWSLOT_PERMISSION_NODE = "rutility.showslot"

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            register(dispatcher)
        }
    }

    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
            CommandManager.literal("showslot")
                .then(
                    CommandManager.argument("slot", IntegerArgumentType.integer(1, 6))
                        .requires(Permissions.require(SHOWSLOT_PERMISSION_NODE, 2))
                        .executes { ctx -> run(ctx) })
        )
    }

    private fun run(ctx: CommandContext<ServerCommandSource>): Int {
        try {
            if (ctx.source.player != null) {
                val player = ctx.source.player
                val partyStore = Cobblemon.storage.getParty(ctx.source.player!!)
                val slot = IntegerArgumentType.getInteger(ctx, "slot")
                val pokemon = partyStore.get(slot - 1)
                if (pokemon != null) {
                    val toSend = player!!.displayName?.copy()?.append(Text.of(": "))?.formatted(Formatting.WHITE)
                    val pokemonName = pokemon.species.translatedName.formatted(Formatting.GREEN).append(" ")
                    toSend?.append(pokemonName)
                    if (pokemon.shiny) {
                        toSend?.append(Text.literal("★ ").formatted(Formatting.GOLD))
                    }
                    if (toSend != null) {
                        ShowUtils.getHoverText(toSend, pokemon)
                    }
                    ctx.source.server.playerManager.playerList.forEach(Consumer { serverPlayer: ServerPlayerEntity ->
                        serverPlayer.sendMessage(
                            toSend
                        )
                    })
                } else {
                    ctx.source.sendError(Text.literal("No Pokemon in slot."))
                }
            } else {
                ctx.source.sendError(Text.of("Sorry, this is only for players."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ctx.source.sendError(Text.of("An error occurred while executing the command."))
        }
        return 1
    }

}