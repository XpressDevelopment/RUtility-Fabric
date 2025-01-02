package network.roanoke.rutility.modules.runningshoes

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.runningshoes.commands.RunningShoesCommand
import network.roanoke.rutility.modules.runningshoes.events.EquipmentChangeEvent

class RunningShoes(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        RunningShoesCommand(this)
        // ServerEntityEvents.EQUIPMENT_CHANGE.register(EquipmentChangeEvent(this))
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}