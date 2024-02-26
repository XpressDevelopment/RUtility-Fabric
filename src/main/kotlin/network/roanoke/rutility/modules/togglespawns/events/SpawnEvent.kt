package network.roanoke.rutility.modules.togglespawns.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.entity.Entity
import network.roanoke.rutility.modules.togglespawns.ToggleSpawns
import network.roanoke.rutility.utils.Utils

class SpawnEvent(private val module: ToggleSpawns) {

    init {
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(priority = Priority.HIGH) {

            if (!module.isEnabled())
                return@subscribe

            // it.ctx.spawner.name -> player name
            val player = it.ctx.spawner.name
            val uuid = Utils.getPlayerByName(player)?.uuid

            if (module.isToggled(uuid!!))
                it.cancel()
//                it.entity.remove(Entity.RemovalReason.DISCARDED) // Will produce console warning spam
        }
    }

}