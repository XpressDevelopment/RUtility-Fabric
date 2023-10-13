package network.roanoke.rutility.modules.levellock.commands

import com.cobblemon.mod.common.util.party
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.levellock.LevelLock


class LevelLockCommand(private val module: LevelLock) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("levellock")
                    .then(CommandManager.argument("slot", IntegerArgumentType.integer()).executes(lockPokemonLevel()))
            )
        })
    }

    private fun lockPokemonLevel(): Command<ServerCommandSource> {
        return Command {
            val source = it.source
            if (!module.isEnabled()) {
                source.sendMessage(Text.literal("§cLevelLock is disabled"))
                return@Command 1
            }

            val player = source.player!!
            var slot = IntegerArgumentType.getInteger(it, "slot")

            if (slot < 1 || slot > 6) {
                source.sendMessage(Text.literal("§cInvalid slot"))
                return@Command 1
            }
            slot -= 1

            val pokemon = player.party().get(slot)

            if (pokemon == null) {
                source.sendMessage(Text.literal("§cNo pokemon in slot ${slot + 1}"))
                return@Command 1
            }

            if (pokemon.persistentData.contains("levellock")) {
                pokemon.persistentData.remove("levellock")
                source.sendMessage(Text.literal("§aUnlocked level for ${pokemon.species.name}"))
                return@Command 1
            } else {
                pokemon.persistentData.putBoolean("levellock", true)
                source.sendMessage(Text.literal("§aLocked level for ${pokemon.species.name}"))
                return@Command 1
            }
        }
    }

}