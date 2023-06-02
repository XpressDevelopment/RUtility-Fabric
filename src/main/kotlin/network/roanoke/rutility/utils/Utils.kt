package network.roanoke.rutility.utils

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.RUtility
import java.util.*

class Utils {

    companion object {
        fun sendMessageToAllPlayers(message: String) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    player.sendMessage(Text.literal(message), false)
                }
            }
        }

        fun getPlayerByUUID(uuid: UUID): ServerPlayerEntity? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.getPlayer(uuid)
        }
    }


}