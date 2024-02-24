package network.roanoke.rutility.modules.show

import com.cobblemon.mod.common.Cobblemon.storage
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.utils.ShowUtils
import java.util.function.Consumer


class ShowParty (override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            register(dispatcher)
        }
    }
    // Permission node for the Show Party command.
    val SHOWPARTY_PERMISSION_NODE = "rutility.showparty"
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
                val partyStore = storage.getParty(player!!.uuid)
                val toSend = player.displayName.copy().append(Text.of(": ")).formatted(Formatting.WHITE)
                toSend.append(ShowUtils.displayParty(partyStore))

                // If toSend is null or empty, it means the party was empty
                if (toSend != null && !toSend.toString().isEmpty()) {
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

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}

