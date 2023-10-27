package network.roanoke.rutility.modules.levellock.commands

import com.cobblemon.mod.common.util.party
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.impactdev.impactor.api.economy.EconomyService
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.levellock.LevelLock


class LevelLockCommand(private val module: LevelLock) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("levellock")
                    .then(
                        CommandManager.argument("slot", IntegerArgumentType.integer())
                            .executes(lockPokemonLevel())
                            .then(
                                CommandManager.argument("level", IntegerArgumentType.integer())
                                    .executes(lockPokemonLevel())
                            )
                    )
                    .then(
                        CommandManager.literal("reload").requires { it.hasPermissionLevel(2) }
                            .executes {
                                module.loadConfig()
                                it.source.sendFeedback({ Text.literal("§aReloaded LevelLock Config") }, false)
                                return@executes 1
                            })
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
            var level = 0
            try {
                level = IntegerArgumentType.getInteger(it, "level")
                if (level < 1 || level > 100) {
                    source.sendMessage(Text.literal("§cInvalid level"))
                    return@Command 1
                }
            } catch (_: IllegalArgumentException) {
            }

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

            if (level != 0) {
                if (!pokemon.persistentData.contains("levellock")) {
                    if (level < pokemon.level) {
                        source.sendMessage(Text.literal("§cYou can't lock your pokemon to a lower level"))
                        return@Command 1
                    }

                    if (EconomyService.instance().account(player.uuid).get().balanceAsync()
                            .get() < module.levelLockCost
                    ) {
                        source.sendMessage(Text.literal("§cYou can't afford to lock your pokemon to a level. You need $${module.levelLockCost.toInt()}"))
                        return@Command 1
                    } else
                        EconomyService.instance().account(player.uuid).get().withdrawAsync(module.levelLockCost)
                }
            }

            if (pokemon.persistentData.contains("levellock")) {
                pokemon.persistentData.remove("levellock")
                source.sendMessage(Text.literal("§aUnlocked level for ${pokemon.species.name}"))
                return@Command 1
            } else {
                pokemon.persistentData.putInt("levellock", level)
                source.sendMessage(Text.literal("§a${if (level != 0) "Paid $${module.levelLockCost.toInt()} and l" else "L"}ocked level for ${pokemon.species.name}${if (level != 0) ". It will not go over level $level" else ""}"))
                return@Command 1
            }
        }
    }
}