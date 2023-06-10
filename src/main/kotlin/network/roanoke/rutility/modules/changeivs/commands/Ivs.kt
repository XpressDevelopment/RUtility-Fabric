package network.roanoke.rutility.modules.changeivs.commands

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.changeivs.ChangeIVs
import network.roanoke.rutility.utils.Utils

class Ivs(private val module: ChangeIVs) {
    init {
        // LMAO AY LISTEN I KNOW IT LOOKS BAD BUT JUST LOOK PAST IT PLEASE
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("ivs").requires { it.hasPermissionLevel(2) }
                    .then(
                        CommandManager.argument("player", StringArgumentType.string())
                            .suggests(playerSuggestionProvider())
                            .then(
                                CommandManager.argument("slot", IntegerArgumentType.integer())
                                    .then(
                                        CommandManager.argument("hp", IntegerArgumentType.integer())
                                            .then(
                                                CommandManager.argument("atk", IntegerArgumentType.integer())
                                                    .then(
                                                        CommandManager.argument("def", IntegerArgumentType.integer())
                                                            .then(
                                                                CommandManager.argument(
                                                                    "spAtk",
                                                                    IntegerArgumentType.integer()
                                                                )
                                                                    .then(
                                                                        CommandManager.argument(
                                                                            "spDef",
                                                                            IntegerArgumentType.integer()
                                                                        )
                                                                            .then(
                                                                                CommandManager.argument(
                                                                                    "spd",
                                                                                    IntegerArgumentType.integer()
                                                                                ).executes(setIvs())
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
        })
    }

    private fun setIvs(): Command<ServerCommandSource>? {
        return Command { context ->
            val source = context.source

            val playerName = StringArgumentType.getString(context, "player")
            val player = Utils.getPlayerByName(playerName)
            if (player == null) {
                source.sendFeedback(Text.literal("§cInvalid player"), false)
                return@Command 0
            }

            val slot = IntegerArgumentType.getInteger(context, "slot")

            val hp = IntegerArgumentType.getInteger(context, "hp")
            val atk = IntegerArgumentType.getInteger(context, "atk")
            val def = IntegerArgumentType.getInteger(context, "def")
            val spAtk = IntegerArgumentType.getInteger(context, "spAtk")
            val spDef = IntegerArgumentType.getInteger(context, "spDef")
            val spd = IntegerArgumentType.getInteger(context, "spd")

            val pokemon = Cobblemon.storage.getParty(player).get(slot - 1)
            pokemon?.ivs?.forEach {
                pokemon.ivs[it.key] = when (it.key.displayName.string) {
                    "HP" -> hp
                    "Attack" -> atk
                    "Defense" -> def
                    "Special Attack" -> spAtk
                    "Special Defense" -> spDef
                    "Speed" -> spd
                    else -> 1
                }
            }

            source.sendFeedback(Text.literal("§aChanged $playerName's ${pokemon?.species?.name}'s IVs."), false)
            1
        }
    }

    private fun playerSuggestionProvider(): SuggestionProvider<ServerCommandSource>? {
        return SuggestionProvider { _: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder ->
            val playerNames = Utils.getAllPlayerNames()

            playerNames!!.forEach {
                builder.suggest(it)
            }

            builder.buildFuture()
        }
    }
}