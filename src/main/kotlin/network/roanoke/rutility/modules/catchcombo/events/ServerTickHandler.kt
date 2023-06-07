package network.roanoke.rutility.modules.catchcombo.events

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.spawning.CobblemonWorldSpawnerManager
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.utils.Utils
import java.util.*

class ServerTickHandler(private val main: RUtility, private val module: CatchCombo) : ServerTickEvents.StartTick {

    private val random = Random()
    private val lastSpawn: MutableMap<UUID, PokemonEntity> = mutableMapOf()

    override fun onStartTick(server: MinecraftServer?) {
        if (!module.isEnabled())
            return

        CobblemonWorldSpawnerManager.spawnersForPlayers.forEach { (uuid, playerSpawner) ->
            if (playerSpawner.spawnedEntities.isEmpty())
                return

            val entity = playerSpawner.spawnedEntities.last() as PokemonEntity
            if (module.comboPokemon.containsKey(uuid) && module.comboCategory.containsKey(uuid) && entity.pokemon.species.name == module.comboPokemon[uuid]
                && entity != lastSpawn[uuid]
            ) {
                if (!entity.pokemon.shiny) {
                    if(random.nextDouble() <= module.comboCategory[uuid]!!.getShinyChance() / Cobblemon.config.shinyRate)
                        entity.pokemon.shiny = true
                }
                lastSpawn[uuid] = entity
            }
        }
    }

}