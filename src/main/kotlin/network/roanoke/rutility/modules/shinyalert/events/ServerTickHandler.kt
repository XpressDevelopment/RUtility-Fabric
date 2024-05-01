package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.util.removeIf
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils

class ServerTickHandler(private val module: ShinyAlert) {

    init {
        var tick = 0
        ServerTickEvents.START_SERVER_TICK.register {
            if (!module.isEnabled())
                return@register

            if (module.faintedPokemon.isNotEmpty()) {
                module.faintedPokemon.forEach { (uuid, ticks) ->
                    module.faintedPokemon[uuid] = ticks - 1
                }
                module.faintedPokemon.removeIf { (_, ticks) -> ticks <= 0 }
            }

            tick++
        }
    }

}