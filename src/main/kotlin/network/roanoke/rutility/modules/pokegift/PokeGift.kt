package network.roanoke.rutility.modules.pokegift

import com.cobblemon.mod.common.Cobblemon.storage
import com.cobblemon.mod.common.battles.BattleRegistry.getBattleByParticipatingPlayer
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility

class PokeGift (override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            register(dispatcher)
        }
    }

    // Permission node for the PokeGift command.
    val POKEGIFT_PERMISSION_NODE = "rutility.pokegift"
    private fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
            CommandManager.literal("pokegift")
                .requires(Permissions.require(POKEGIFT_PERMISSION_NODE, 2))
                .then(CommandManager.argument("player", StringArgumentType.word())
                    .suggests { context: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder? ->
                        CommandSource.suggestMatching(
                            context.source.server.playerNames,
                            builder
                        )
                    } // Suggest online players
                    .then(
                        CommandManager.argument("slot", IntegerArgumentType.integer(1, 6))
                            .executes { context: CommandContext<ServerCommandSource> ->
                                val sender = context.source.player
                                val targetPlayerName = StringArgumentType.getString(context, "player")
                                val targetPlayer =
                                    context.source.server.playerManager.getPlayer(targetPlayerName)
                                if (targetPlayer == null) {
                                    context.source.sendError(Text.of("That player is not online!"))
                                    return@executes 0
                                }

                                // Check if sender is not targeting themselves
                                if (sender!!.uuid == targetPlayer.uuid) {
                                    context.source.sendError(Text.of("You cannot PokeGift to yourself!"))
                                    return@executes 0
                                }
                                val slot = IntegerArgumentType.getInteger(context, "slot")
                                run(context, targetPlayer, slot)
                            }
                    )
                )
        )
    }

    private fun run(context: CommandContext<ServerCommandSource>, targetPlayer: ServerPlayerEntity, slot: Int): Int {
        return try {
            val sender = context.source.player

            // Check if the player is in battle.
            if (getBattleByParticipatingPlayer(sender!!) != null) {
                context.source.sendError(Text.of("You cannot use PokeGift while in a battle!"))
                return 0
            }

            // Step 1: Retrieve the Pokemon in Player1's selected slot and store it in memory
            val senderPartyStore = storage.getParty(sender.uuid)
            val adjustedSlot = slot - 1 // Adjust for 0-based index
            val pokemonToGift = senderPartyStore.get(adjustedSlot)
            if (pokemonToGift == null) {
                context.source.sendError(Text.of("There's no Pokemon in the selected slot!"))
                return 0
            }

            /* Ensure that Pokemon isn't in a battle or has other conditions that prevent it from being transferred.
                if (pokemonToGift.isInBattle()) {
                    System.out.println("Debug: Pokemon is currently in battle. Exiting.");
                    context.getSource().sendError(Text.of("You cannot gift a Pokemon that is in battle!"));
                    return 0;
                }
              */

            // Step 2: Remove the Pokemon from Player1's party.
            if (!senderPartyStore.remove(pokemonToGift)) {
                context.source.sendError(Text.of("Unexpected error occurred! Couldn't remove the Pokemon from your party."))
                return 0
            }

            // Step 3: Try to add the Pokemon to Player2's party. If the party is full, move to their PC.
            val targetPartyStore = storage.getParty(targetPlayer.uuid)
            val addedToParty = targetPartyStore.add(pokemonToGift)
            if (!addedToParty) {
                // Party is full, try to add to PC
                val targetPCStore = storage.getPC(targetPlayer.uuid)
                val addedToPC = targetPCStore.add(pokemonToGift)
                if (!addedToPC) {
                    context.source.sendError(Text.of("The target player's PC is full. The Pokemon could not be transferred."))
                    return 0
                }
            }

            // Step 4: Send confirmation messages to both players.
            val pokemonName = pokemonToGift.getDisplayName().string
            val senderMessage =
                Text.of(Formatting.GREEN.toString() + "You have gifted " + pokemonName + " to " + targetPlayer.name.string)
            val targetMessage =
                Text.of(Formatting.GREEN.toString() + sender.name.string + " has gifted you their " + pokemonName)
            sender.sendMessage(senderMessage, false)
            targetPlayer.sendMessage(targetMessage, false)
            1
        } catch (e: Exception) {
            // Catch any other exceptions that may be occurring.
            println("Debug: Unexpected exception caught during command execution.")
            e.printStackTrace()
            context.source.sendError(Text.of("An unexpected error occurred during the command execution."))
            0
        }
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}
