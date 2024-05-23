package network.roanoke.rutility.modules.magikarp.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.world.ServerWorld
import network.roanoke.rutility.modules.magikarp.Magikarp
import network.roanoke.rutility.modules.magikarp.objects.Magikarpd
import network.roanoke.rutility.utils.Utils

class SpawnEvent(private val module: Magikarp) {

    init {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(priority = Priority.HIGH) { event ->

            if (!module.isEnabled())
                return@subscribe

            val player = event.ctx.spawner.name
            val uuid = Utils.getPlayerByName(player)?.uuid

            val globalMagikarpd = module.magikarpManager.globalQueue

            for (magikarp in globalMagikarpd) {
                if (magikarp.activated) {
                    event.cancel()
                    PokemonSpecies.getByName("magikarp")!!.create(event.entity.pokemon.level).sendOut(event.entity.world as ServerWorld, event.entity.pos, null) {
                        if (event.entity.pokemon.shiny)
                            it.pokemon.shiny = true
                        if (Math.random() <= 0.01)
                            PokemonProperties.parse("star").apply(it)
                        it.setPosition(Utils.checkBlocksAbove(event.entity).toCenterPos())
                    }
                }
            }

            if (uuid != null) {
                val playerMagikarpd = module.magikarpManager.playerQueues[uuid.toString()] ?: return@subscribe

                for (magikarp in playerMagikarpd) {
                    if (magikarp.activated) {
                        event.cancel()
                        PokemonSpecies.getByName("magikarp")!!.create(event.entity.pokemon.level).sendOut(event.entity.world as ServerWorld, event.entity.pos, null) {
                            if (event.entity.pokemon.shiny)
                                it.pokemon.shiny = true
                            if (Math.random() <= 0.01)
                                PokemonProperties.parse("star").apply(it)
                            it.setPosition(Utils.checkBlocksAbove(event.entity).toCenterPos())
                        }
                    }
                }
            }
        }
    }

}