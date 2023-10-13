package network.roanoke.rutility.modules.levellock

import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.levellock.commands.LevelLockCommand
import network.roanoke.rutility.modules.levellock.events.LevelUpEvent

class LevelLock(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        LevelLockCommand(this)
        LevelUpEvent(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}