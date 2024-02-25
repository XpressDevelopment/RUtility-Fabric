package network.roanoke.rutility.modules.battlegodmode.events

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import network.roanoke.rutility.modules.battlegodmode.BattleGodMode

class PlayerDisconnect(private val module: BattleGodMode) : ServerPlayConnectionEvents.Disconnect {
    override fun onPlayDisconnect(handler: ServerPlayNetworkHandler?, server: MinecraftServer?) {
        if (module.isEnabled()) {
            if (handler != null) {
                val player = handler.player
                if (module.playersInBattle.contains(player.uuid)) {
                    module.playersInBattle.remove(player.uuid)
                }
            }
        }
    }

}