package network.roanoke.rutility.modules.magikarp.objects

import com.cobblemon.mod.common.CobblemonSounds
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.utils.Utils
import java.util.*

class Magikarpd(val duration: Int, var completedTicks: Int = 0, var purchaser: String = "Server", var target: String) {

    var activated = false

    fun activate() {
        if (target == "global") {
            Utils.broadcastTitle("§4You have been Magikarp'd")
            Utils.broadcast(Text.literal("§4Server has been Magikarp'd by §6$purchaser §4for §6" + (duration/1200) + " §4minutes."))
            Utils.playSoundAll(CobblemonSounds.IMPACT_WATER)
        } else {
            val player = Utils.getPlayerByUUID(UUID.fromString(target)) ?: return
            Utils.sendTitle(player, Text.literal("§4You've been Magikarp'd"))
            Utils.broadcastSubtitleAllBut(Text.literal("").append(player.name).append(" has been Magikarp'd").formatted(
                Formatting.RED), player)
            player.sendMessage(Text.literal("§4You've been Magikarp'd by §6$purchaser §4for §6" + (duration/1200) + " §4minutes."))
            Utils.playSoundPlayer(player, CobblemonSounds.IMPACT_WATER)
        }
        activated = true
    }

    fun kill () {
        if (target == "global") {
            Utils.broadcastTitle("§4You are no longer Magikarp'd")
            Utils.broadcast(Text.literal("§4Server is no longer Magikarp'd."))
        } else {
            val player = Utils.getPlayerByUUID(UUID.fromString(target)) ?: return
            Utils.sendTitle(player, Text.literal("§4You are no longer Magikarp'd"))
            Utils.broadcastSubtitleAllBut(Text.literal("").append(player.name).append(" is no longer Magikarp'd").formatted(
                Formatting.RED), player)
            player.sendMessage(Text.literal("§4You are no longer Magikarp'd."))
        }
        activated = false
    }

    fun isDone(): Boolean {
        return completedTicks >= duration
    }

    fun incrementTick() {
        completedTicks++
    }

}