package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils

class FaintEvent(private val module: ShinyAlert) {

        init {
             CobblemonEvents.POKEMON_FAINTED.subscribe {
                 if (!module.isEnabled())
                     return@subscribe

                 if (it.pokemon.shiny && it.pokemon.isWild()) {
                     if (module.faintedPokemon.contains(it.pokemon.uuid.toString()))
                         return@subscribe

                     Utils.broadcast(
                         "§b(!) §4Shiny ${it.pokemon.species.name} has been defeated..."
                     )
                     module.faintedPokemon[it.pokemon.uuid.toString()] = 20 * 10
                 }
             }
        }
}