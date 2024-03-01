package network.roanoke.rutility.modules.show.commands

import com.cobblemon.mod.common.Cobblemon
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.modules.show.ShowParty
import network.roanoke.rutility.utils.ShowUtils
import java.util.function.Consumer

class CShowParty(private val module: ShowParty) {
    // Permission node for the Show Party command.
    val SHOWPARTY_PERMISSION_NODE = "rutility.showparty"

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            register(dispatcher)
        }
    }

    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
            CommandManager.literal("showparty")
                .requires(Permissions.require(SHOWPARTY_PERMISSION_NODE, 2))
                .executes { ctx -> run(ctx) })
    }

    private fun run(ctx: CommandContext<ServerCommandSource>): Int {
        try {
            if (ctx.source.player != null) {
                val player = ctx.source.player
                val partyStore = Cobblemon.storage.getParty(player!!.uuid)
                val toSend = player.displayName.copy().append(Text.of(": ")).formatted(Formatting.WHITE)
                toSend.append(ShowUtils.displayParty(partyStore))

                // If toSend is null or empty, it means the party was empty
                if (toSend != null && toSend.toString().isNotEmpty()) {
                    ctx.source.server.playerManager.playerList.forEach(Consumer { serverPlayer: ServerPlayerEntity ->
                        serverPlayer.sendMessage(
                            toSend
                        )
                    })
                } else {
                    ctx.source.sendError(Text.literal("No Pokemon in party."))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ctx.source.sendError(Text.of("An error occurred while executing the command."))
        }
        return 1
    }

}