package network.roanoke.rutility.modules.togglespawns.commands

import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import network.roanoke.rutility.modules.togglespawns.ToggleSpawns

class CToggleSpawns(private val module: ToggleSpawns) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("togglespawns").executes(toggleSpawns()))
        })
    }

    private fun toggleSpawns(): Command<ServerCommandSource>? {
        return Command {
            val uuid = it.source.player!!.uuid
            module.togglePlayer(uuid)
            val spawnsEnabled = !module.isToggled(uuid)
            module.saveToggledPlayers()
            it.source.sendMessage(Text.literal(if (spawnsEnabled) "§aEnabled Pokemon Spawning" else "§aDisabled Pokemon Spawning"))
            //it.source.sendFeedback(Text.literal(if (spawnsEnabled) "§aEnabled Pokemon Spawning" else "§aDisabled Pokemon Spawning"), false)
            1
        }
    }

}