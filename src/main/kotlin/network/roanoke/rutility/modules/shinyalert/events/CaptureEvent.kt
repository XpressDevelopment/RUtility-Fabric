package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils

class CaptureEvent(private val module: ShinyAlert) {
    init {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(priority = Priority.LOWEST) {
            if (!module.isEnabled())
                return@subscribe

            if (it.pokemon.shiny) {
                Utils.broadcast(
                    Text.literal("§b(!) ").append(it.player.name).formatted(Formatting.AQUA)
                        .append(" §ehas caught a §bShiny ${it.pokemon.species.name}§e!")

                            // this does not work with catch combos
//                        .append(
//                            " §ehas caught a §bShiny ${it.pokemon.species.name} " +
//                                    "§ewith §b${
//                                        String.format(
//                                            "%.2f",
//                                            (Utils.getTotalIVs(it.pokemon) / 510f) * 100
//                                        )
//                                    }% §eIVs!"
//                        )
                )
            }

        }
    }

}