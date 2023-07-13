package network.roanoke.rutility.modules.togglespawns.events

import com.cobblemon.mod.common.api.spawning.CobblemonWorldSpawnerManager
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.togglespawns.ToggleSpawns

class ServerTickHandler(private val main: RUtility, private val module: ToggleSpawns) : ServerTickEvents.StartTick {

    override fun onStartTick(server: MinecraftServer?) {
        if (!module.isEnabled())
            return

        CobblemonWorldSpawnerManager.spawnersForPlayers.forEach { (uuid, playerSpawner) ->
            playerSpawner.active = !module.isToggled(uuid)

            if (playerSpawner.spawnedEntities.isEmpty())
                return
        }
    }

}