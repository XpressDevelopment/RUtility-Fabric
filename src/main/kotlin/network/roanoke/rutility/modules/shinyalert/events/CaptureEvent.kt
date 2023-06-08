package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.text.Text
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.modules.catchcombo.ComboCategory
import network.roanoke.rutility.modules.shinyalert.ShinyAlert

class CaptureEvent(private val module: ShinyAlert) {
    init {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(priority = Priority.NORMAL) { it ->
            if (!module.isEnabled())
                return@subscribe

            val uuid = it.player.uuid

            val list: MutableList<Pokemon> =
                if (module.shinyEntities[uuid] == null) mutableListOf() else module.shinyEntities[uuid]!!

            if (list.contains(it.pokemon)) {
                list.remove(it.pokemon)
                module.shinyEntities[uuid] = list
            }

        }
    }

}