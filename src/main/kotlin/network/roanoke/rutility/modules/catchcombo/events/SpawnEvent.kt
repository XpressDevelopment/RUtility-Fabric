package network.roanoke.rutility.modules.catchcombo.events

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.utils.Utils
import java.util.*

class SpawnEvent(private val module: CatchCombo) {

    private val random = Random()

    init {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(priority = Priority.HIGHEST) {
            if (!module.isEnabled())
                return@subscribe

            // it.ctx.spawner.name -> player name
            val player = it.ctx.spawner.name
            val uuid = Utils.getPlayerByName(player)?.uuid
            val entity = it.entity

            val shinyChance = random.nextDouble()

            if (module.comboPokemon.containsKey(uuid) && module.comboCategory.containsKey(uuid) && entity.pokemon.species.name == module.comboPokemon[uuid]
            ) {
                if (!entity.pokemon.shiny) {
                    if(shinyChance <= module.comboCategory[uuid]!!.getShinyChance() / Cobblemon.config.shinyRate)
                        entity.pokemon.shiny = true
                }
            }
        }
    }

}