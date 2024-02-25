package network.roanoke.rutility.modules.battlegodmode

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.battlegodmode.events.BattleEvents
import network.roanoke.rutility.modules.battlegodmode.events.EntityDamageEvent
import network.roanoke.rutility.modules.battlegodmode.events.PlayerDisconnect
import java.util.*

class BattleGodMode(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    private val _playersInBattle: MutableList<UUID> = mutableListOf()
    val playersInBattle: MutableList<UUID>
        get() = _playersInBattle

    init {
        BattleEvents(this)
        ServerPlayConnectionEvents.DISCONNECT.register(PlayerDisconnect(this))
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityDamageEvent(this))
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }

}