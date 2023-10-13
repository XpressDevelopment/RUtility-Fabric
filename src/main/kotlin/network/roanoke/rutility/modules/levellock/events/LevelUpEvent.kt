package network.roanoke.rutility.modules.levellock.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import network.roanoke.rutility.modules.levellock.LevelLock

class LevelUpEvent(private val module: LevelLock) {

    init {
        CobblemonEvents.LEVEL_UP_EVENT.subscribe { event ->
            if (!module.isEnabled())
                return@subscribe

            val pokemon = event.pokemon
            if (pokemon.persistentData.contains("levellock")) {
                event.newLevel = event.oldLevel
            }
        }
    }

}