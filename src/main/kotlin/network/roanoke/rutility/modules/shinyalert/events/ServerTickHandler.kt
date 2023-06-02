package network.roanoke.rutility.modules.shinyalert.events

import com.cobblemon.mod.common.api.spawning.CobblemonWorldSpawnerManager
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.utils.Utils
import java.util.*

class ServerTickHandler(private val module: ShinyAlert) : ServerTickEvents.StartTick {

    private val lastSpawn: MutableMap<UUID, PokemonEntity> = mutableMapOf()

    
    override fun onStartTick(server: MinecraftServer?) {
        if (!module.isEnabled())
            return

        CobblemonWorldSpawnerManager.spawnersForPlayers.forEach { (uuid, playerSpawner) ->
            if (playerSpawner.spawnedEntities.isEmpty())
                return

            val entity = playerSpawner.spawnedEntities.last() as PokemonEntity
            if (entity != lastSpawn[uuid] && entity.pokemon.shiny) {
                Utils.sendMessageToAllPlayers("§b(!) §eA §bShiny ${entity.pokemon.species.name} §ehas spawned on §b${Utils.getPlayerByUUID(uuid)?.name?.string}§e!")
                lastSpawn[uuid] = entity
            }
        }
    }

}