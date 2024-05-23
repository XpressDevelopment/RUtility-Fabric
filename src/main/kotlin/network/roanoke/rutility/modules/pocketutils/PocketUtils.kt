package network.roanoke.rutility.modules.pocketutils

import com.cobblemon.mod.common.util.removeIf
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.pocketutils.events.UseItemEvent

class PocketUtils(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    private val cooldownUsers = mutableMapOf<String, Int>()
    private val healingCooldownUsers = mutableMapOf<String, Int>()

    init {
        UseItemCallback.EVENT.register(UseItemEvent(this))

        ServerTickEvents.START_SERVER_TICK.register {
            cooldownUsers.forEach {
                if (it.value > 0)
                    cooldownUsers[it.key] = it.value - 1
            }

            healingCooldownUsers.forEach {
                if (it.value > 0)
                    healingCooldownUsers[it.key] = it.value - 1
            }

            cooldownUsers.removeIf { it.value <= 0 }
            healingCooldownUsers.removeIf { it.value <= 0 }
        }
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }

    fun setCooldownUser(uuid: String, time: Int) {
        cooldownUsers[uuid] = time
    }

    fun isOnCooldown(uuid: String): Boolean {
        return cooldownUsers.containsKey(uuid)
    }

    fun setHealingCooldownUser(uuid: String, time: Int) {
        healingCooldownUsers[uuid] = time
    }

    fun isHealingOnCooldown(uuid: String): Boolean {
        return healingCooldownUsers.containsKey(uuid)
    }

}