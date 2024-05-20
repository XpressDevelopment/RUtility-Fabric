package network.roanoke.rutility.modules.magikarp.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.magikarp.Magikarp
import network.roanoke.rutility.utils.Utils
import java.util.*

class MagikarpCommand(private val module: Magikarp) {
    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("magikarp")
                    .then(
                        CommandManager.literal("global").requires(Permissions.require("rutility.magikarp.create", 2))
                            .then(
                                CommandManager.argument("duration", IntegerArgumentType.integer(1, 1440))
                                    .executes(magikarpGlobal())
                            )
                    )
                    .then(
                        CommandManager.literal("player").requires(Permissions.require("rutility.magikarp.create", 2))
                            .then(
                                CommandManager.argument("player", StringArgumentType.string())
                                    .then(
                                        CommandManager.argument("duration", IntegerArgumentType.integer(1, 1440))
                                            .executes(magikarpPlayer())
                                    )
                            )
                    )
                    .then(
                        CommandManager.literal("info").requires(Permissions.require("rutility.magikarp.info", 2))
                            .executes(magikarpInfo())
                    )
            )
        })
    }

    private fun magikarpInfo(): Command<ServerCommandSource> {
        return Command {
            val source = it.source
            if (!module.isEnabled()) {
                source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                return@Command 1
            }

            val globalMagikarpd = module.magikarpManager.globalQueue
            val playerMagikarpd = module.magikarpManager.playerQueues

            source.sendFeedback({ Text.literal("§6Global Magikarp'd:") }, false)
            for (magikarp in globalMagikarpd) {
                source.sendFeedback({ Text.literal("-§e" + magikarp.purchaser + " §6for §e" + (magikarp.duration / 1200) + " §6minutes") }, false)
            }

            source.sendFeedback({ Text.literal("§6Player Magikarp'd:") }, false)
            playerMagikarpd.forEach { (uuid, queue) ->
                val player = Utils.getPlayerByUUID(UUID.fromString(uuid))
                source.sendFeedback({ Text.literal("-").append(player?.name ?: Text.literal("OFFLINE")).append(Text.literal(":")) }, false)
                for (magikarp in queue) {
                    source.sendFeedback({ Text.literal("--§e" + magikarp.purchaser + " §6for §e" + (magikarp.duration / 1200) + " §6minutes") }, false)
                }
            }

            1
        }
    }

    private fun magikarpGlobal(): Command<ServerCommandSource> {
        return Command {
            val source = it.source
            if (!module.isEnabled()) {
                source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                return@Command 1
            }

            val duration = IntegerArgumentType.getInteger(it, "duration")
            module.magikarpManager.createMagikarpd(duration * 20 * 60, source.name, "global")

            source.sendFeedback({ Text.literal("§aMagikarp'd the entire server.") }, true)
            1
        }
    }

    private fun magikarpPlayer(): Command<ServerCommandSource> {
        return Command {
            val source = it.source
            if (!module.isEnabled()) {
                source.sendFeedback({ Text.literal("§cModule is disabled") }, false)
                return@Command 1
            }

            val p = StringArgumentType.getString(it, "player")
            val player = Utils.getPlayerByName(p)
            if (player == null) {
                source.sendFeedback({ Text.literal("§cPlayer not found") }, false)
                return@Command 1
            }
            val duration = IntegerArgumentType.getInteger(it, "duration")

            module.magikarpManager.createMagikarpd(duration * 20 * 60, source.name, player.uuid.toString())
            source.sendFeedback({ Text.literal("§aMagikarp'd ").append(player.name) }, true)
            1
        }
    }
}