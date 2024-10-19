package network.roanoke.rutility.modules.bonkstick.commands

import com.mojang.brigadier.arguments.StringArgumentType
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.modules.bonkstick.BonkStick
import network.roanoke.rutility.modules.bonkstick.items.Stick
import network.roanoke.rutility.utils.Utils

class BonkStickCommand(private val module: BonkStick) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("bonkstick").requires(Permissions.require("rutility.bonkstick", 2))
                    .then(
                        CommandManager.literal("give").requires(Permissions.require("rutility.bonkstick.give", 2))
                            .then(
                                CommandManager.argument("player", StringArgumentType.string())
                                    .executes { ctx ->
                                        if (module.isEnabled()) {
                                            val player =
                                                Utils.getPlayerByName(StringArgumentType.getString(ctx, "player"))
                                                    ?: return@executes 1
                                            player.giveItemStack(Stick.getBonkStick())
                                            ctx.source.sendFeedback({
                                                Text.literal("§aGave Bonk Stick to ").append(ctx.source.displayName)
                                            }, true)
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