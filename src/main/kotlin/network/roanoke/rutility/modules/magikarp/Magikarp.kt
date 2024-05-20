package network.roanoke.rutility.modules.magikarp

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.magikarp.commands.MagikarpCommand
import network.roanoke.rutility.modules.magikarp.events.SpawnEvent
import network.roanoke.rutility.modules.magikarp.managers.MagikarpManager

class Magikarp(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    val magikarpManager = MagikarpManager(this)

    init {
        MagikarpCommand(this)
        SpawnEvent(this)

        ServerTickEvents.START_SERVER_TICK.register {
            magikarpManager.update(it)
        }
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
    }

}