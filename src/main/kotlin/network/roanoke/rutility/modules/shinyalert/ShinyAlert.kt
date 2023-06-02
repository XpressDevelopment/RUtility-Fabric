package network.roanoke.rutility.modules.shinyalert

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.shinyalert.events.ServerTickHandler

class ShinyAlert(private val main: RUtility, override val name: String): RModule {
    private var enabled = false

    init {
        ServerTickEvents.START_SERVER_TICK.register(ServerTickHandler(this))
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}