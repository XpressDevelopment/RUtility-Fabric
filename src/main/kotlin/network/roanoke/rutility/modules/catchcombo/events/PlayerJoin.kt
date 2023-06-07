package network.roanoke.rutility.modules.catchcombo.events

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.text.Text
import network.roanoke.rutility.modules.catchcombo.CatchCombo

class PlayerJoin(private val module: CatchCombo) : ServerPlayConnectionEvents.Join {
    override fun onPlayReady(handler: ServerPlayNetworkHandler?, sender: PacketSender?, server: MinecraftServer?) {
        if (!module.isEnabled())
            return

        val player = handler?.player
        val uuid = player!!.uuid
        if (module.comboAmount.containsKey(uuid))
            player.sendMessage(Text.literal("${module.comboPokemon[uuid]} Catch Combo: ${module.comboAmount[uuid]}"))
    }
}