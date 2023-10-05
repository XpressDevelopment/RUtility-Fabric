package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils
import java.util.*

class SpawnEvent(private val module: ShinyAlert) {


    init {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(priority = Priority.LOWEST) {
            if (!module.isEnabled())
                return@subscribe

            if (it.entity.pokemon.shiny) {
                Utils.sendMessageToAllPlayers(
                    "§b(!) §eA §bShiny ${it.entity.pokemon.species.name} §ehas spawned on §b${it.ctx.spawner.name}§e!"
                )
            }
        }
    }

}