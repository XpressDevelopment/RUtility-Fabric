package network.roanoke.rutility.modules.catchcombo.commands

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.utils.Utils

class Combo(private val module: CatchCombo) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("catchcombo")
                    .then(CommandManager.literal("reload").requires { it.hasPermissionLevel(2) }).executes(reloadCombos())
                    .then(CommandManager.literal("set").requires { it.hasPermissionLevel(2) }
                        .then(
                            CommandManager.argument("player", StringArgumentType.string())
                                .suggests(playerSuggestionProvider())
                                .then(
                                    CommandManager.argument("species", StringArgumentType.string())
                                        .suggests(speciesSuggestionProvider())
                                        .then(
                                            CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(setCombo())
                                        )
                                )
                        )
                    ))
        })
    }

    private fun reloadCombos(): Command<ServerCommandSource>? {
        return Command {
            module.comboConfig.loadPlayerCombos()
            1
        }
    }

    private fun setCombo(): Command<ServerCommandSource>? {
        return Command { context: CommandContext<ServerCommandSource> ->
            val source = context.source

            if (!module.isEnabled()) {
                source.sendFeedback(Text.literal("§Module is disabled"), false)
                return@Command 0
            }

            val playerName = StringArgumentType.getString(context, "player")
            val species = StringArgumentType.getString(context, "species")
            val amount = IntegerArgumentType.getInteger(context, "amount")

            val player = Utils.getPlayerByName(playerName)
            if (player == null) {
                source.sendFeedback(Text.literal("§cInvalid player"), false)
                return@Command 0
            }

            var found = false
            PokemonSpecies.species.forEach {
                if (species.equals(it.name, ignoreCase = false))
                    found = true
            }

            if (!found) {
                source.sendFeedback(Text.literal("§cInvalid species (Case Sensitive)"), false)
                return@Command 0
            }

            module.setComboAmount(player.uuid, amount)
            module.setComboSpecies(player.uuid, species)

            module.comboConfig.savePlayerCombo(player.uuid)

            source.sendFeedback(Text.literal("§aSet $playerName's Catch Combo of $species to $amount."), true)
            1
        }
    }

    private fun speciesSuggestionProvider(): SuggestionProvider<ServerCommandSource>? {
        return SuggestionProvider { _: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder ->
            val speciesList = PokemonSpecies.species

            speciesList.forEach {
                builder.suggest(it.name)
            }

            builder.buildFuture()
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