package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.text.Text
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils

class SpawnEvent(private val module: ShinyAlert) {


    init {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(priority = Priority.LOWEST) {
            if (!module.isEnabled())
                return@subscribe

            if (it.entity.pokemon.shiny) {
                Utils.broadcast(
                    Text.literal("§b(!) §eA §bShiny ").append(it.entity.pokemon.getDisplayName())
                        .append(Text.literal(" §ehas spawned on §b${it.ctx.spawner.name}§e!"))
                )
            }
        }
    }

}