package network.roanoke.rutility.modules.battlegodmode.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import network.roanoke.rutility.modules.battlegodmode.BattleGodMode

class BattleEvents(private val module: BattleGodMode) {

    init {
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe { event ->
            if (module.isEnabled()) {
                event.battle.players.forEach { player ->
                    module.playersInBattle.add(player.uuid)
                }
            }
        }

        CobblemonEvents.BATTLE_VICTORY.subscribe { event ->
            if (module.isEnabled()) {
                event.battle.players.forEach {
                    module.playersInBattle.remove(it.uuid)
                }
            }
        }

        CobblemonEvents.BATTLE_FLED.subscribe { event ->
            if (module.isEnabled()) {
                module.playersInBattle.remove(event.player.uuid)
            }
        }
    }

}