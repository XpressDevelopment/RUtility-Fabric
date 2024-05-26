package network.roanoke.rutility.modules.bonkstick.commands

import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.modules.bonkstick.BonkStick
import network.roanoke.rutility.modules.bonkstick.items.Stick

class BonkStickCommand(private val module: BonkStick) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("bonkstick").requires(Permissions.require("rutility.bonkstick", 2))
                    .then(
                        CommandManager.literal("give").requires(Permissions.require("rutility.bonkstick.give", 2))
                            .executes { ctx ->
                                if (module.isEnabled()) {

                                    if (ctx.source.player != null) {
                                        val player = ctx.source.player as ServerPlayerEntity
                                        player.giveItemStack(Stick.getBonkStick())
                                        ctx.source.sendFeedback({ Text.literal("§aGave Bonk Stick to ").append(ctx.source.displayName) }, true)
                                    } else
                                        ctx.source.sendFeedback({ Text.literal("§cYou must be a player to use this command") }, false)
                                } else {
                                    ctx.source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                                }
                                1
                            }
                    )
            )
        })
    }

}