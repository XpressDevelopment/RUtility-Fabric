package network.roanoke.rutility.utils

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.RUtility
import java.util.*

class Utils {

    companion object {
        fun broadcast(message: Any) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    player.sendMessage(Text.literal(message.toString()), false)
                }
            }
        }

        fun getPlayerByUUID(uuid: UUID): ServerPlayerEntity? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.getPlayer(uuid)
        }

        fun getPlayerByName(name: String): ServerPlayerEntity? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.getPlayer(name)
        }

        fun getAllPlayerNames(): Array<out String>? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.playerNames
        }
    }


}